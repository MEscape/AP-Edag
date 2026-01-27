-- V8__create_skill_development_triggers.sql
-- Creates triggers and functions to automatically maintain skill_development table
-- Tracks monthly skill growth metrics and updates snapshots automatically

-- Function to update or create skill development snapshot for current month
CREATE OR REPLACE FUNCTION update_skill_development_snapshot()
    RETURNS TRIGGER AS $$
DECLARE
    v_employee_id UUID;
    v_current_month DATE;
    v_total_skills INTEGER;
    v_top_category VARCHAR(128);
    v_previous_month_skills INTEGER;
    v_skills_added INTEGER;
    v_skills_updated INTEGER;
    v_skills_removed INTEGER;
BEGIN
    -- Determine which employee was affected
    IF TG_OP = 'DELETE' THEN
        v_employee_id := OLD.employee_id;
    ELSE
        v_employee_id := NEW.employee_id;
    END IF;

    -- Get first day of current month
    v_current_month := DATE_TRUNC('month', CURRENT_DATE)::DATE;

    -- Calculate current metrics
    SELECT COUNT(*)
    INTO v_total_skills
    FROM employee_skills
    WHERE employee_id = v_employee_id;

    -- Find top category (most skills in that category)
    SELECT sc.name
    INTO v_top_category
    FROM skill_categories sc
             INNER JOIN skills s ON s.category_id = sc.id
             INNER JOIN employee_skills es ON es.skill_id = s.id
    WHERE es.employee_id = v_employee_id
    GROUP BY sc.name
    ORDER BY COUNT(*) DESC
    LIMIT 1;

    -- Get previous month's total for comparison
    SELECT total_skills
    INTO v_previous_month_skills
    FROM skill_development
    WHERE user_id = v_employee_id
      AND month = v_current_month
    LIMIT 1;

    -- Calculate deltas based on operation type
    IF TG_OP = 'INSERT' THEN
        v_skills_added := 1;
        v_skills_updated := 0;
        v_skills_removed := 0;
    ELSIF TG_OP = 'UPDATE' THEN
        v_skills_added := 0;
        v_skills_updated := 1;
        v_skills_removed := 0;
    ELSIF TG_OP = 'DELETE' THEN
        v_skills_added := 0;
        v_skills_updated := 0;
        v_skills_removed := 1;
    END IF;

    -- Insert or update current month's snapshot
    INSERT INTO skill_development (
        user_id,
        month,
        total_skills,
        skills_added,
        skills_updated,
        skills_removed,
        top_category
    ) VALUES (
                 v_employee_id,
                 v_current_month,
                 COALESCE(v_total_skills, 0),
                 v_skills_added,
                 v_skills_updated,
                 v_skills_removed,
                 v_top_category
             )
    ON CONFLICT (user_id, month) DO UPDATE
        SET total_skills = COALESCE(v_total_skills, 0),
            skills_added = skill_development.skills_added + v_skills_added,
            skills_updated = skill_development.skills_updated + v_skills_updated,
            skills_removed = skill_development.skills_removed + v_skills_removed,
            top_category = v_top_category;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Trigger for skill insertions
CREATE TRIGGER trigger_skill_development_insert
    AFTER INSERT ON employee_skills
    FOR EACH ROW
EXECUTE FUNCTION update_skill_development_snapshot();

-- Trigger for skill updates
CREATE TRIGGER trigger_skill_development_update
    AFTER UPDATE ON employee_skills
    FOR EACH ROW
EXECUTE FUNCTION update_skill_development_snapshot();

-- Trigger for skill deletions
CREATE TRIGGER trigger_skill_development_delete
    AFTER DELETE ON employee_skills
    FOR EACH ROW
EXECUTE FUNCTION update_skill_development_snapshot();

COMMENT ON FUNCTION update_skill_development_snapshot() IS 'Automatically maintains monthly skill development snapshots';

-- Function to initialize historical snapshots for existing employees
CREATE OR REPLACE FUNCTION initialize_skill_development_history()
    RETURNS void AS $$
DECLARE
    v_employee RECORD;
    v_month DATE;
BEGIN
    -- For each employee with skills
    FOR v_employee IN
        SELECT DISTINCT employee_id
        FROM employee_skills
        LOOP
            -- Get the earliest skill date or profile creation date
            SELECT COALESCE(MIN(es.last_used), MIN(ep.created_at::DATE))
            INTO v_month
            FROM employee_skills es
                     LEFT JOIN employee_profiles ep ON ep.user_id = es.employee_id
            WHERE es.employee_id = v_employee.employee_id;

            -- If we have a date, start from that month
            IF v_month IS NOT NULL THEN
                v_month := DATE_TRUNC('month', v_month)::DATE;

                -- Create snapshots for each month up to current month
                WHILE v_month <= DATE_TRUNC('month', CURRENT_DATE)::DATE LOOP
                        PERFORM update_skill_development_snapshot_for_month(v_employee.employee_id, v_month);
                        v_month := v_month + INTERVAL '1 month';
                    END LOOP;
            END IF;
        END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Helper function to create snapshot for specific month
CREATE OR REPLACE FUNCTION update_skill_development_snapshot_for_month(
    p_employee_id UUID,
    p_month DATE
)
    RETURNS void AS $$
DECLARE
    v_total_skills INTEGER;
    v_top_category VARCHAR(128);
BEGIN
    -- Calculate metrics for skills that existed in that month
    SELECT COUNT(*)
    INTO v_total_skills
    FROM employee_skills
    WHERE employee_id = p_employee_id
      AND (last_used IS NULL OR last_used >= p_month);

    -- Find top category
    SELECT sc.name
    INTO v_top_category
    FROM skill_categories sc
             INNER JOIN skills s ON s.category_id = sc.id
             INNER JOIN employee_skills es ON es.skill_id = s.id
    WHERE es.employee_id = p_employee_id
      AND (es.last_used IS NULL OR es.last_used >= p_month)
    GROUP BY sc.name
    ORDER BY COUNT(*) DESC
    LIMIT 1;

    -- Insert snapshot
    INSERT INTO skill_development (
        user_id,
        month,
        total_skills,
        skills_added,
        skills_updated,
        skills_removed,
        top_category
    ) VALUES (
                 p_employee_id,
                 p_month,
                 COALESCE(v_total_skills, 0),
                 0, -- Historical data doesn't track deltas
                 0,
                 0,
                 v_top_category
             )
    ON CONFLICT (user_id, month) DO NOTHING;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION initialize_skill_development_history() IS 'One-time function to backfill historical skill development data';
COMMENT ON FUNCTION update_skill_development_snapshot_for_month(UUID, DATE) IS 'Creates or updates skill development snapshot for a specific month';
