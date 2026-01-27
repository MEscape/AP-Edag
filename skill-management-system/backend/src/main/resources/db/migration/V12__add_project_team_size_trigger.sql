-- V12__add_project_team_size_trigger.sql
-- Adds automatic calculation of team_size based on project_members count

-- Function to calculate team size
CREATE OR REPLACE FUNCTION calculate_project_team_size()
RETURNS TRIGGER AS $$
BEGIN
    -- Update the team_size of the project based on the number of members
    UPDATE projects
    SET team_size = (
        SELECT COUNT(DISTINCT employee_id)
        FROM project_members
        WHERE project_id = COALESCE(NEW.project_id, OLD.project_id)
    )
    WHERE id = COALESCE(NEW.project_id, OLD.project_id);

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Trigger on INSERT of project members
CREATE TRIGGER trg_project_member_insert_team_size
AFTER INSERT ON project_members
FOR EACH ROW
EXECUTE FUNCTION calculate_project_team_size();

-- Trigger on DELETE of project members
CREATE TRIGGER trg_project_member_delete_team_size
AFTER DELETE ON project_members
FOR EACH ROW
EXECUTE FUNCTION calculate_project_team_size();

-- Initial calculation for existing projects
UPDATE projects p
SET team_size = (
    SELECT COUNT(DISTINCT pm.employee_id)
    FROM project_members pm
    WHERE pm.project_id = p.id
);

COMMENT ON FUNCTION calculate_project_team_size() IS 'Automatically calculates and updates team_size based on distinct project members';
