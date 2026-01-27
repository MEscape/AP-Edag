-- V5__create_update_timestamp_triggers.sql
-- Creates triggers to automatically update updated_at timestamps
-- Ensures audit fields are always accurate without application-level management

-- Generic function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION update_updated_at_column() IS 'Automatically updates updated_at timestamp on row modification';

-- Apply trigger to users table
CREATE TRIGGER trigger_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Apply trigger to positions table
CREATE TRIGGER trigger_positions_updated_at
    BEFORE UPDATE ON positions
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Apply trigger to locations table
CREATE TRIGGER trigger_locations_updated_at
    BEFORE UPDATE ON locations
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Apply trigger to skill_categories table
CREATE TRIGGER trigger_skill_categories_updated_at
    BEFORE UPDATE ON skill_categories
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Apply trigger to skills table
CREATE TRIGGER trigger_skills_updated_at
    BEFORE UPDATE ON skills
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Apply trigger to employee_profiles table
CREATE TRIGGER trigger_employee_profiles_updated_at
    BEFORE UPDATE ON employee_profiles
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Apply trigger to projects table
CREATE TRIGGER trigger_projects_updated_at
    BEFORE UPDATE ON projects
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
