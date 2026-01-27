-- V15__extend_activity_logging_triggers.sql
-- Extends activity logging to cover role requests and project members
-- Adds comprehensive audit trail for role management and project team changes

-- ============================================================================
-- ROLE REQUEST ACTIVITY LOGGING
-- ============================================================================

-- Function to log role request activities
CREATE OR REPLACE FUNCTION log_role_request_activity()
    RETURNS TRIGGER AS $$
DECLARE
    v_activity_type VARCHAR(64);
BEGIN
    IF TG_OP = 'INSERT' THEN
        v_activity_type := 'ROLE_REQUEST_CREATED';
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (NEW.user_id, v_activity_type, NEW.id, 'ROLE_REQUEST', CURRENT_TIMESTAMP);

    ELSIF TG_OP = 'UPDATE' THEN
        -- Log status changes
        IF OLD.status IS DISTINCT FROM NEW.status THEN
            CASE NEW.status
                WHEN 'APPROVED' THEN
                    v_activity_type := 'ROLE_REQUEST_APPROVED';
                    -- Log activity for both requester and approver
                    INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
                    VALUES (NEW.user_id, v_activity_type, NEW.id, 'ROLE_REQUEST', CURRENT_TIMESTAMP);

                    IF NEW.reviewed_by IS NOT NULL THEN
                        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
                        VALUES (NEW.reviewed_by, 'ROLE_REQUEST_REVIEWED', NEW.id, 'ROLE_REQUEST', CURRENT_TIMESTAMP);
                    END IF;

                WHEN 'REJECTED' THEN
                    v_activity_type := 'ROLE_REQUEST_REJECTED';
                    -- Log activity for both requester and rejecter
                    INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
                    VALUES (NEW.user_id, v_activity_type, NEW.id, 'ROLE_REQUEST', CURRENT_TIMESTAMP);

                    IF NEW.reviewed_by IS NOT NULL THEN
                        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
                        VALUES (NEW.reviewed_by, 'ROLE_REQUEST_REVIEWED', NEW.id, 'ROLE_REQUEST', CURRENT_TIMESTAMP);
                    END IF;

                ELSE
                    v_activity_type := 'ROLE_REQUEST_UPDATED';
                    INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
                    VALUES (NEW.user_id, v_activity_type, NEW.id, 'ROLE_REQUEST', CURRENT_TIMESTAMP);
                END CASE;
        END IF;

    ELSIF TG_OP = 'DELETE' THEN
        v_activity_type := 'ROLE_REQUEST_CANCELLED';
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (OLD.user_id, v_activity_type, OLD.id, 'ROLE_REQUEST', CURRENT_TIMESTAMP);
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trigger_log_role_request_insert ON role_requests;
CREATE TRIGGER trigger_log_role_request_insert
    AFTER INSERT ON role_requests
    FOR EACH ROW
EXECUTE FUNCTION log_role_request_activity();

DROP TRIGGER IF EXISTS trigger_log_role_request_update ON role_requests;
CREATE TRIGGER trigger_log_role_request_update
    AFTER UPDATE ON role_requests
    FOR EACH ROW
EXECUTE FUNCTION log_role_request_activity();

DROP TRIGGER IF EXISTS trigger_log_role_request_delete ON role_requests;
CREATE TRIGGER trigger_log_role_request_delete
    AFTER DELETE ON role_requests
    FOR EACH ROW
EXECUTE FUNCTION log_role_request_activity();

COMMENT ON FUNCTION log_role_request_activity() IS 'Logs role request lifecycle events including creation, approval, rejection, and cancellation';

-- ============================================================================
-- PROJECT MEMBER ACTIVITY LOGGING
-- ============================================================================

-- Function to log project member activities
CREATE OR REPLACE FUNCTION log_project_member_activity()
    RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        -- Log activity for the employee who was added to the project
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (NEW.employee_id, 'PROJECT_MEMBER_ADDED', NEW.project_id, 'PROJECT', CURRENT_TIMESTAMP);

    ELSIF TG_OP = 'UPDATE' THEN
        -- Log if position/role changed
        IF OLD.position_id IS DISTINCT FROM NEW.position_id THEN
            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (NEW.employee_id, 'PROJECT_MEMBER_ROLE_CHANGED', NEW.project_id, 'PROJECT', CURRENT_TIMESTAMP);
        END IF;

    ELSIF TG_OP = 'DELETE' THEN
        -- Log activity for the employee who was removed from the project
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (OLD.employee_id, 'PROJECT_MEMBER_REMOVED', OLD.project_id, 'PROJECT', CURRENT_TIMESTAMP);
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trigger_log_project_member_insert ON project_members;
CREATE TRIGGER trigger_log_project_member_insert
    AFTER INSERT ON project_members
    FOR EACH ROW
EXECUTE FUNCTION log_project_member_activity();

DROP TRIGGER IF EXISTS trigger_log_project_member_update ON project_members;
CREATE TRIGGER trigger_log_project_member_update
    AFTER UPDATE ON project_members
    FOR EACH ROW
EXECUTE FUNCTION log_project_member_activity();

DROP TRIGGER IF EXISTS trigger_log_project_member_delete ON project_members;
CREATE TRIGGER trigger_log_project_member_delete
    AFTER DELETE ON project_members
    FOR EACH ROW
EXECUTE FUNCTION log_project_member_activity();

COMMENT ON FUNCTION log_project_member_activity() IS 'Logs project team membership changes including additions, role changes, and removals';

-- ============================================================================
-- ENHANCED PROJECT ACTIVITY LOGGING
-- ============================================================================

-- Update existing project activity function to work with new structure
CREATE OR REPLACE FUNCTION log_project_activity()
    RETURNS TRIGGER AS $$
DECLARE
    v_activity_type VARCHAR(64);
    v_creator_id UUID;
BEGIN
    IF TG_OP = 'INSERT' THEN
        v_activity_type := 'PROJECT_CREATED';
        -- Log for the creator if available
        IF NEW.created_by_user_id IS NOT NULL THEN
            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (NEW.created_by_user_id, v_activity_type, NEW.id, 'PROJECT', CURRENT_TIMESTAMP);
        END IF;

    ELSIF TG_OP = 'UPDATE' THEN
        -- Log status changes
        IF OLD.status IS DISTINCT FROM NEW.status THEN
            CASE NEW.status
                WHEN 'ACTIVE' THEN v_activity_type := 'PROJECT_STARTED';
                WHEN 'COMPLETED' THEN v_activity_type := 'PROJECT_COMPLETED';
                WHEN 'PLANNED' THEN v_activity_type := 'PROJECT_PLANNED';
                ELSE v_activity_type := 'PROJECT_UPDATED';
                END CASE;

            -- Log for the creator
            IF NEW.created_by_user_id IS NOT NULL THEN
                INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
                VALUES (NEW.created_by_user_id, v_activity_type, NEW.id, 'PROJECT', CURRENT_TIMESTAMP);
            END IF;
        END IF;

        -- Log significant field changes (not status)
        IF OLD.status = NEW.status AND (
            OLD.name IS DISTINCT FROM NEW.name OR
            OLD.description IS DISTINCT FROM NEW.description OR
            OLD.start_date IS DISTINCT FROM NEW.start_date OR
            OLD.end_date IS DISTINCT FROM NEW.end_date
            ) THEN
            v_activity_type := 'PROJECT_UPDATED';
            IF NEW.created_by_user_id IS NOT NULL THEN
                INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
                VALUES (NEW.created_by_user_id, v_activity_type, NEW.id, 'PROJECT', CURRENT_TIMESTAMP);
            END IF;
        END IF;

    ELSIF TG_OP = 'DELETE' THEN
        v_activity_type := 'PROJECT_DELETED';
        -- Log for the creator
        IF OLD.created_by_user_id IS NOT NULL THEN
            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (OLD.created_by_user_id, v_activity_type, OLD.id, 'PROJECT', CURRENT_TIMESTAMP);
        END IF;
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Drop and recreate project triggers (using DROP IF EXISTS to handle existing triggers)
DROP TRIGGER IF EXISTS trigger_log_project_insert ON projects;
CREATE TRIGGER trigger_log_project_insert
    AFTER INSERT ON projects
    FOR EACH ROW
EXECUTE FUNCTION log_project_activity();

DROP TRIGGER IF EXISTS trigger_log_project_update ON projects;
CREATE TRIGGER trigger_log_project_update
    AFTER UPDATE ON projects
    FOR EACH ROW
EXECUTE FUNCTION log_project_activity();

DROP TRIGGER IF EXISTS trigger_log_project_delete ON projects;
CREATE TRIGGER trigger_log_project_delete
    AFTER DELETE ON projects
    FOR EACH ROW
EXECUTE FUNCTION log_project_activity();

COMMENT ON FUNCTION log_project_activity() IS 'Logs project-related activities including creation, status changes, updates, and deletion';

-- ============================================================================
-- PROJECT SKILL ACTIVITY LOGGING
-- ============================================================================

-- Function to log project skill/technology changes
CREATE OR REPLACE FUNCTION log_project_skill_activity()
    RETURNS TRIGGER AS $$
DECLARE
    v_creator_id UUID;
BEGIN
    -- Get project creator for activity logging
    SELECT created_by_user_id INTO v_creator_id
    FROM projects
    WHERE id = COALESCE(NEW.project_id, OLD.project_id);

    IF v_creator_id IS NOT NULL THEN
        IF TG_OP = 'INSERT' THEN
            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (v_creator_id, 'PROJECT_SKILL_ADDED', NEW.project_id, 'PROJECT', CURRENT_TIMESTAMP);

        ELSIF TG_OP = 'DELETE' THEN
            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (v_creator_id, 'PROJECT_SKILL_REMOVED', OLD.project_id, 'PROJECT', CURRENT_TIMESTAMP);
        END IF;
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trigger_log_project_skill_insert ON project_skills;
CREATE TRIGGER trigger_log_project_skill_insert
    AFTER INSERT ON project_skills
    FOR EACH ROW
EXECUTE FUNCTION log_project_skill_activity();

DROP TRIGGER IF EXISTS trigger_log_project_skill_delete ON project_skills;
CREATE TRIGGER trigger_log_project_skill_delete
    AFTER DELETE ON project_skills
    FOR EACH ROW
EXECUTE FUNCTION log_project_skill_activity();

COMMENT ON FUNCTION log_project_skill_activity() IS 'Logs changes to project required skills/technologies';

-- ============================================================================
-- USER ACCOUNT ACTIVITY LOGGING
-- ============================================================================

-- Function to log user account lifecycle events
CREATE OR REPLACE FUNCTION log_user_account_activity()
    RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (NEW.id, 'USER_ACCOUNT_CREATED', NEW.id, 'USER', CURRENT_TIMESTAMP);

    ELSIF TG_OP = 'UPDATE' THEN
        -- Log significant profile changes
        IF OLD.email IS DISTINCT FROM NEW.email THEN
            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (NEW.id, 'USER_EMAIL_CHANGED', NEW.id, 'USER', CURRENT_TIMESTAMP);
        END IF;

        IF (OLD.first_name IS DISTINCT FROM NEW.first_name) OR
           (OLD.last_name IS DISTINCT FROM NEW.last_name) THEN
            INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
            VALUES (NEW.id, 'USER_NAME_CHANGED', NEW.id, 'USER', CURRENT_TIMESTAMP);
        END IF;

    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO activities (user_id, type, entity_id, entity_type, timestamp)
        VALUES (OLD.id, 'USER_ACCOUNT_DELETED', OLD.id, 'USER', CURRENT_TIMESTAMP);
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trigger_log_user_account_insert ON users;
CREATE TRIGGER trigger_log_user_account_insert
    AFTER INSERT ON users
    FOR EACH ROW
EXECUTE FUNCTION log_user_account_activity();

DROP TRIGGER IF EXISTS trigger_log_user_account_update ON users;
CREATE TRIGGER trigger_log_user_account_update
    AFTER UPDATE ON users
    FOR EACH ROW
EXECUTE FUNCTION log_user_account_activity();

DROP TRIGGER IF EXISTS trigger_log_user_account_delete ON users;
CREATE TRIGGER trigger_log_user_account_delete
    AFTER DELETE ON users
    FOR EACH ROW
EXECUTE FUNCTION log_user_account_activity();

COMMENT ON FUNCTION log_user_account_activity() IS 'Logs user account lifecycle events and profile changes';

-- ============================================================================
-- ACTIVITY TYPE DOCUMENTATION
-- ============================================================================

COMMENT ON TABLE activities IS 'Comprehensive audit log of all user activities including:
- SKILL_ADDED, SKILL_UPDATED, SKILL_REMOVED: Skill management
- PROJECT_CREATED, PROJECT_STARTED, PROJECT_COMPLETED, PROJECT_UPDATED, PROJECT_DELETED: Project lifecycle
- PROJECT_MEMBER_ADDED, PROJECT_MEMBER_REMOVED, PROJECT_MEMBER_ROLE_CHANGED: Team management
- PROJECT_SKILL_ADDED, PROJECT_SKILL_REMOVED: Project technology changes
- ROLE_REQUEST_CREATED, ROLE_REQUEST_APPROVED, ROLE_REQUEST_REJECTED, ROLE_REQUEST_CANCELLED, ROLE_REQUEST_REVIEWED: Role management
- PROFILE_CREATED, PROFILE_UPDATED: Profile changes
- USER_ACCOUNT_CREATED, USER_EMAIL_CHANGED, USER_NAME_CHANGED, USER_ACCOUNT_DELETED: Account management';
