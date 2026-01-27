-- V10__create_employee_profile_on_user_insert.sql
-- Creates trigger to automatically initialize employee_profile when a user is created
-- Profile is created with NULL values for all fields except user_id
-- User must complete their profile to appear in employee discovery

-- Function to initialize employee profile when user is created
CREATE OR REPLACE FUNCTION initialize_employee_profile_on_user_insert()
    RETURNS TRIGGER AS $$
BEGIN
    -- Create employee_profile record with only user_id set
    -- All other fields are NULL and must be filled by the user
    INSERT INTO employee_profiles (
        user_id,
        position_id,
        location_id,
        availability,
        years_of_experience,
        bio,
        created_at,
        updated_at
    )
    VALUES (
        NEW.id,
        NULL, -- Must be set by user
        NULL, -- Must be set by user
        'UNAVAILABLE',
        0,
        NULL,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    )
    ON CONFLICT (user_id) DO NOTHING;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION initialize_employee_profile_on_user_insert() IS
    'Automatically creates empty employee_profile when a new user is created';

-- Create trigger on users table
CREATE TRIGGER trigger_initialize_employee_profile_on_user_insert
    AFTER INSERT ON users
    FOR EACH ROW
EXECUTE FUNCTION initialize_employee_profile_on_user_insert();

COMMENT ON TRIGGER trigger_initialize_employee_profile_on_user_insert ON users IS
    'Ensures every new user gets an employee_profile that must be completed';
