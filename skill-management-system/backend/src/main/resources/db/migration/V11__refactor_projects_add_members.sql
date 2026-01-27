-- V11__refactor_projects_add_members.sql
-- Refactors projects table to remove employee-specific fields
-- Creates project_members junction table for many-to-many relationship
-- This eliminates redundancy when multiple employees work on the same project

-- Step 1: Drop dependent triggers first
DROP TRIGGER IF EXISTS trigger_update_project_counts_insert ON projects;
DROP TRIGGER IF EXISTS trigger_update_project_counts_update ON projects;
DROP TRIGGER IF EXISTS trigger_update_project_counts_delete ON projects;

DROP TRIGGER IF EXISTS trigger_log_project_insert ON projects;
DROP TRIGGER IF EXISTS trigger_log_project_update ON projects;
DROP TRIGGER IF EXISTS trigger_log_project_delete ON projects;

-- Step 2: Create project_members table
CREATE TABLE project_members (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 project_id UUID NOT NULL,
                                 employee_id UUID NOT NULL,
                                 position_id UUID NOT NULL,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_project_member_project FOREIGN KEY (project_id)
                                     REFERENCES projects(id) ON DELETE CASCADE,
                                 CONSTRAINT fk_project_member_employee FOREIGN KEY (employee_id)
                                     REFERENCES employee_profiles(user_id) ON DELETE CASCADE,
                                 CONSTRAINT fk_project_member_position FOREIGN KEY (position_id)
                                     REFERENCES positions(id) ON DELETE RESTRICT,
                                 CONSTRAINT uk_project_employee UNIQUE (project_id, employee_id)
);

CREATE INDEX idx_project_members_project ON project_members(project_id);
CREATE INDEX idx_project_members_employee ON project_members(employee_id);
CREATE INDEX idx_project_members_position ON project_members(position_id);

COMMENT ON TABLE project_members IS 'Junction table for project team members and their roles';
COMMENT ON COLUMN project_members.position_id IS 'Role/position held by employee in this project';

-- Step 3: Migrate existing data from projects to project_members
INSERT INTO project_members (project_id, employee_id, position_id)
SELECT id, employee_id, position_id
FROM projects
WHERE employee_id IS NOT NULL;

-- Step 4: Add created_by_user_id to projects
ALTER TABLE projects
    ADD COLUMN created_by_user_id UUID;

ALTER TABLE projects
    ADD CONSTRAINT fk_project_created_by_user
        FOREIGN KEY (created_by_user_id)
            REFERENCES users(id) ON DELETE SET NULL;

CREATE INDEX idx_projects_created_by_user ON projects(created_by_user_id);

COMMENT ON COLUMN projects.created_by_user_id IS 'User (manager) who created this project';

-- Step 5: Drop old indexes
DROP INDEX IF EXISTS idx_projects_employee_start_date;
DROP INDEX IF EXISTS idx_projects_employee_status;

-- Step 6: Remove employee_id and position_id from projects
ALTER TABLE projects
    DROP CONSTRAINT IF EXISTS fk_project_employee,
    DROP CONSTRAINT IF EXISTS fk_project_position,
    DROP COLUMN employee_id,
    DROP COLUMN position_id;

-- Step 7: Recreate update_project_counts function with new logic
CREATE OR REPLACE FUNCTION update_project_counts()
    RETURNS TRIGGER AS $$
DECLARE
    v_employee_id UUID;
    v_total_projects INTEGER;
    v_active_projects INTEGER;
    affected_employees UUID[];
BEGIN
    -- Collect all affected employee IDs based on operation type
    IF TG_OP = 'DELETE' THEN
        -- When a project is deleted, get all members
        SELECT ARRAY_AGG(DISTINCT employee_id)
        INTO affected_employees
        FROM project_members
        WHERE project_id = OLD.id;
    ELSIF TG_OP = 'UPDATE' AND OLD.status IS DISTINCT FROM NEW.status THEN
        -- When project status changes, get all members
        SELECT ARRAY_AGG(DISTINCT employee_id)
        INTO affected_employees
        FROM project_members
        WHERE project_id = NEW.id;
    ELSIF TG_OP = 'INSERT' THEN
        -- When a project is created, get all members (though there may be none yet)
        SELECT ARRAY_AGG(DISTINCT employee_id)
        INTO affected_employees
        FROM project_members
        WHERE project_id = NEW.id;
    END IF;

    -- Update statistics for each affected employee
    IF affected_employees IS NOT NULL THEN
        FOREACH v_employee_id IN ARRAY affected_employees
            LOOP
                -- Calculate project counts for this employee
                SELECT
                    COUNT(DISTINCT p.id),
                    COUNT(DISTINCT p.id) FILTER (WHERE p.status = 'ACTIVE')
                INTO v_total_projects, v_active_projects
                FROM projects p
                         INNER JOIN project_members pm ON pm.project_id = p.id
                WHERE pm.employee_id = v_employee_id;

                -- Update statistics
                INSERT INTO user_statistics (user_id, total_projects, active_projects, last_calculated)
                VALUES (v_employee_id, COALESCE(v_total_projects, 0), COALESCE(v_active_projects, 0), CURRENT_TIMESTAMP)
                ON CONFLICT (user_id) DO UPDATE
                    SET total_projects = COALESCE(v_total_projects, 0),
                        active_projects = COALESCE(v_active_projects, 0),
                        last_calculated = CURRENT_TIMESTAMP;
            END LOOP;
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Step 8: Recreate log_project_activity function with new logic
CREATE OR REPLACE FUNCTION log_project_activity()
    RETURNS TRIGGER AS $$
DECLARE
    v_activity_type VARCHAR(64);
    v_employee_id UUID;
BEGIN
    IF TG_OP = 'INSERT' THEN
        v_activity_type := 'PROJECT_CREATED';
        -- Log for creator if available, or first member added later
        IF NEW.created_by_user_id IS NOT NULL THEN
            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (NEW.created_by_user_id, v_activity_type, NEW.id, 'PROJECT', CURRENT_TIMESTAMP);
        END IF;
    ELSIF TG_OP = 'UPDATE' THEN
        IF OLD.status <> NEW.status THEN
            CASE NEW.status
                WHEN 'ACTIVE' THEN v_activity_type := 'PROJECT_STARTED';
                WHEN 'COMPLETED' THEN v_activity_type := 'PROJECT_COMPLETED';
                WHEN 'PLANNED' THEN v_activity_type := 'PROJECT_PLANNED';
                ELSE v_activity_type := 'PROJECT_UPDATED';
                END CASE;

            -- Log for all project members
            FOR v_employee_id IN
                SELECT employee_id FROM project_members WHERE project_id = NEW.id
                LOOP
                    INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
                    VALUES (v_employee_id, v_activity_type, NEW.id, 'PROJECT', CURRENT_TIMESTAMP);
                END LOOP;
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        v_activity_type := 'PROJECT_DELETED';
        -- Log for all project members
        FOR v_employee_id IN
            SELECT employee_id FROM project_members WHERE project_id = OLD.id
            LOOP
                INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
                VALUES (v_employee_id, v_activity_type, OLD.id, 'PROJECT', CURRENT_TIMESTAMP);
            END LOOP;
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Step 9: Recreate triggers on projects table
CREATE TRIGGER trigger_update_project_counts_insert
    AFTER INSERT ON projects
    FOR EACH ROW
EXECUTE FUNCTION update_project_counts();

CREATE TRIGGER trigger_update_project_counts_update
    AFTER UPDATE ON projects
    FOR EACH ROW
    WHEN (OLD.status IS DISTINCT FROM NEW.status)
EXECUTE FUNCTION update_project_counts();

CREATE TRIGGER trigger_update_project_counts_delete
    AFTER DELETE ON projects
    FOR EACH ROW
EXECUTE FUNCTION update_project_counts();

CREATE TRIGGER trigger_log_project_insert
    AFTER INSERT ON projects
    FOR EACH ROW
EXECUTE FUNCTION log_project_activity();

CREATE TRIGGER trigger_log_project_update
    AFTER UPDATE ON projects
    FOR EACH ROW
EXECUTE FUNCTION log_project_activity();

CREATE TRIGGER trigger_log_project_delete
    AFTER DELETE ON projects
    FOR EACH ROW
EXECUTE FUNCTION log_project_activity();

-- Step 10: Create triggers for project_members table
CREATE OR REPLACE FUNCTION update_project_counts_on_member_change()
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

    -- Calculate project counts for this employee
    SELECT
        COUNT(DISTINCT p.id),
        COUNT(DISTINCT p.id) FILTER (WHERE p.status = 'ACTIVE')
    INTO v_total_projects, v_active_projects
    FROM projects p
             INNER JOIN project_members pm ON pm.project_id = p.id
    WHERE pm.employee_id = v_employee_id;

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

CREATE TRIGGER trigger_update_project_counts_member_insert
    AFTER INSERT ON project_members
    FOR EACH ROW
EXECUTE FUNCTION update_project_counts_on_member_change();

CREATE TRIGGER trigger_update_project_counts_member_delete
    AFTER DELETE ON project_members
    FOR EACH ROW
EXECUTE FUNCTION update_project_counts_on_member_change();

COMMENT ON FUNCTION update_project_counts() IS 'Automatically updates total_projects and active_projects in user_statistics based on project changes';
COMMENT ON FUNCTION update_project_counts_on_member_change() IS 'Automatically updates total_projects and active_projects when project members are added or removed';
COMMENT ON FUNCTION log_project_activity() IS 'Logs project-related activities to activities table for all project members';
