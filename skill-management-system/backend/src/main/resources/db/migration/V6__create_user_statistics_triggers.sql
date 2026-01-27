-- V6__create_user_statistics_triggers.sql
-- Creates triggers to automatically update user_statistics table
-- Keeps statistics in sync with actual data changes in real-time

-- Function to initialize user statistics when employee profile is created
CREATE OR REPLACE FUNCTION initialize_user_statistics()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO user_statistics (user_id)
    VALUES (NEW.user_id)
    ON CONFLICT (user_id) DO NOTHING;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_initialize_user_statistics
    AFTER INSERT ON employee_profiles
    FOR EACH ROW
EXECUTE FUNCTION initialize_user_statistics();

COMMENT ON FUNCTION initialize_user_statistics() IS 'Creates initial user_statistics record when employee profile is created';

-- Function to update total_skills when employee_skills changes
CREATE OR REPLACE FUNCTION update_total_skills()
    RETURNS TRIGGER AS $$
DECLARE
    v_employee_id UUID;
    v_total_skills INTEGER;
    v_avg_score NUMERIC(5,2);
BEGIN
    -- Determine which employee was affected
    IF TG_OP = 'DELETE' THEN
        v_employee_id := OLD.employee_id;
    ELSE
        v_employee_id := NEW.employee_id;
    END IF;

    -- Calculate total skills and average score
    SELECT COUNT(*), AVG(proficiency_score)
    INTO v_total_skills, v_avg_score
    FROM employee_skills
    WHERE employee_id = v_employee_id;

    -- Update or insert statistics
    INSERT INTO user_statistics (user_id, total_skills, average_skill_score, last_calculated)
    VALUES (v_employee_id, COALESCE(v_total_skills, 0), v_avg_score, CURRENT_TIMESTAMP)
    ON CONFLICT (user_id) DO UPDATE
        SET total_skills = COALESCE(v_total_skills, 0),
            average_skill_score = v_avg_score,
            last_calculated = CURRENT_TIMESTAMP;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_total_skills_insert
    AFTER INSERT ON employee_skills
    FOR EACH ROW
EXECUTE FUNCTION update_total_skills();

CREATE TRIGGER trigger_update_total_skills_update
    AFTER UPDATE ON employee_skills
    FOR EACH ROW
EXECUTE FUNCTION update_total_skills();

CREATE TRIGGER trigger_update_total_skills_delete
    AFTER DELETE ON employee_skills
    FOR EACH ROW
EXECUTE FUNCTION update_total_skills();

COMMENT ON FUNCTION update_total_skills() IS 'Automatically updates total_skills and average_skill_score in user_statistics';

-- Function to update project counts when projects change
CREATE OR REPLACE FUNCTION update_project_counts()
    RETURNS TRIGGER AS $$
DECLARE
    v_employee_id UUID;
    v_total_projects INTEGER;
    v_active_projects INTEGER;
BEGIN
    -- Determine which employee was affected
    IF TG_OP = 'DELETE' THEN
        v_employee_id := OLD.employee_id;
    ELSE
        v_employee_id := NEW.employee_id;
    END IF;

    -- Calculate project counts
    SELECT
        COUNT(*),
        COUNT(*) FILTER (WHERE status = 'ACTIVE')
    INTO v_total_projects, v_active_projects
    FROM projects
    WHERE employee_id = v_employee_id;

    -- Update statistics
    INSERT INTO user_statistics (user_id, total_projects, active_projects, last_calculated)
    VALUES (v_employee_id, COALESCE(v_total_projects, 0), COALESCE(v_active_projects, 0), CURRENT_TIMESTAMP)
    ON CONFLICT (user_id) DO UPDATE
        SET total_projects = COALESCE(v_total_projects, 0),
            active_projects = COALESCE(v_active_projects, 0),
            last_calculated = CURRENT_TIMESTAMP;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_project_counts_insert
    AFTER INSERT ON projects
    FOR EACH ROW
EXECUTE FUNCTION update_project_counts();

CREATE TRIGGER trigger_update_project_counts_update
    AFTER UPDATE ON projects
    FOR EACH ROW
    WHEN (OLD.status IS DISTINCT FROM NEW.status OR OLD.employee_id IS DISTINCT FROM NEW.employee_id)
EXECUTE FUNCTION update_project_counts();

CREATE TRIGGER trigger_update_project_counts_delete
    AFTER DELETE ON projects
    FOR EACH ROW
EXECUTE FUNCTION update_project_counts();

COMMENT ON FUNCTION update_project_counts() IS 'Automatically updates total_projects and active_projects in user_statistics';
