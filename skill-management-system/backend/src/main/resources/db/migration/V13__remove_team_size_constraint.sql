-- V13__remove_team_size_constraint.sql
-- Removes the team_size check constraint as the trigger ensures correct values
-- This allows temporary zero values during member updates within transactions

-- Drop the check constraint
ALTER TABLE projects
    DROP CONSTRAINT IF EXISTS chk_project_team_size;

COMMENT ON COLUMN projects.team_size IS
    'Number of team members, automatically calculated by trigger. Can be NULL or >= 0.';
