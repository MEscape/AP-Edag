-- V7__create_activity_logging_triggers.sql
-- Creates triggers to automatically log user activities
-- Provides audit trail for analytics and user history tracking

-- Function to log employee skill activities
CREATE OR REPLACE FUNCTION log_employee_skill_activity()
    RETURNS TRIGGER AS $$
DECLARE
    v_activity_type VARCHAR(64);
BEGIN
    IF TG_OP = 'INSERT' THEN
        v_activity_type := 'SKILL_ADDED';
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (NEW.employee_id, v_activity_type, NEW.skill_id, 'SKILL', CURRENT_TIMESTAMP);
    ELSIF TG_OP = 'UPDATE' THEN
        IF OLD.proficiency_score <> NEW.proficiency_score THEN
            v_activity_type := 'SKILL_UPDATED';
            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (NEW.employee_id, v_activity_type, NEW.skill_id, 'SKILL', CURRENT_TIMESTAMP);
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        v_activity_type := 'SKILL_REMOVED';
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (OLD.employee_id, v_activity_type, OLD.skill_id, 'SKILL', CURRENT_TIMESTAMP);
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_log_employee_skill_insert
    AFTER INSERT ON employee_skills
    FOR EACH ROW
EXECUTE FUNCTION log_employee_skill_activity();

CREATE TRIGGER trigger_log_employee_skill_update
    AFTER UPDATE ON employee_skills
    FOR EACH ROW
EXECUTE FUNCTION log_employee_skill_activity();

CREATE TRIGGER trigger_log_employee_skill_delete
    AFTER DELETE ON employee_skills
    FOR EACH ROW
EXECUTE FUNCTION log_employee_skill_activity();

COMMENT ON FUNCTION log_employee_skill_activity() IS 'Logs skill-related activities to activities table';

-- Function to log project activities
CREATE OR REPLACE FUNCTION log_project_activity()
    RETURNS TRIGGER AS $$
DECLARE
    v_activity_type VARCHAR(64);
BEGIN
    IF TG_OP = 'INSERT' THEN
        v_activity_type := 'PROJECT_CREATED';
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (NEW.employee_id, v_activity_type, NEW.id, 'PROJECT', CURRENT_TIMESTAMP);
    ELSIF TG_OP = 'UPDATE' THEN
        IF OLD.status <> NEW.status THEN
            CASE NEW.status
                WHEN 'ACTIVE' THEN v_activity_type := 'PROJECT_STARTED';
                WHEN 'COMPLETED' THEN v_activity_type := 'PROJECT_COMPLETED';
                WHEN 'PLANNED' THEN v_activity_type := 'PROJECT_PLANNED';
                ELSE v_activity_type := 'PROJECT_UPDATED';
                END CASE;

            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (NEW.employee_id, v_activity_type, NEW.id, 'PROJECT', CURRENT_TIMESTAMP);
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        v_activity_type := 'PROJECT_DELETED';
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (OLD.employee_id, v_activity_type, OLD.id, 'PROJECT', CURRENT_TIMESTAMP);
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

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

COMMENT ON FUNCTION log_project_activity() IS 'Logs project-related activities to activities table';

-- Function to log profile updates
CREATE OR REPLACE FUNCTION log_profile_activity()
    RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (NEW.user_id, 'PROFILE_CREATED', NEW.user_id, 'PROFILE', CURRENT_TIMESTAMP);
    ELSIF TG_OP = 'UPDATE' THEN
        -- Only log if meaningful fields changed
        IF (OLD.position_id IS DISTINCT FROM NEW.position_id OR
            OLD.location_id IS DISTINCT FROM NEW.location_id OR
            OLD.years_of_experience IS DISTINCT FROM NEW.years_of_experience OR
            OLD.availability IS DISTINCT FROM NEW.availability OR
            OLD.bio IS DISTINCT FROM NEW.bio) THEN

            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (NEW.user_id, 'PROFILE_UPDATED', NEW.user_id, 'PROFILE', CURRENT_TIMESTAMP);
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_log_profile_insert
    AFTER INSERT ON employee_profiles
    FOR EACH ROW
EXECUTE FUNCTION log_profile_activity();

CREATE TRIGGER trigger_log_profile_update
    AFTER UPDATE ON employee_profiles
    FOR EACH ROW
EXECUTE FUNCTION log_profile_activity();

COMMENT ON FUNCTION log_profile_activity() IS 'Logs employee profile changes to activities table';
