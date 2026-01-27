-- V2__create_employee_profiles.sql
-- Creates employee_profiles table and employee_skills junction table
-- Employee profiles extend user accounts with organizational details

-- Employee profiles table - one-to-one with users
CREATE TABLE employee_profiles (
                                   user_id UUID PRIMARY KEY,
                                   position_id UUID,
                                   location_id UUID,
                                   availability VARCHAR(50) NOT NULL DEFAULT 'UNAVAILABLE',
                                   years_of_experience NUMERIC(3,1) NOT NULL DEFAULT 0.0,
                                   bio TEXT,
                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   CONSTRAINT fk_employee_user FOREIGN KEY (user_id)
                                       REFERENCES users(id) ON DELETE CASCADE,
                                   CONSTRAINT fk_employee_position FOREIGN KEY (position_id)
                                       REFERENCES positions(id) ON DELETE RESTRICT,
                                   CONSTRAINT fk_employee_location FOREIGN KEY (location_id)
                                       REFERENCES locations(id) ON DELETE RESTRICT,
                                   CONSTRAINT chk_years_of_experience CHECK (years_of_experience >= 0),
                                   CONSTRAINT chk_bio_length CHECK (LENGTH(bio) <= 500)
);

CREATE INDEX idx_employee_profiles_location ON employee_profiles(location_id);
CREATE INDEX idx_employee_profiles_position ON employee_profiles(position_id);
CREATE INDEX idx_employee_profiles_availability ON employee_profiles(availability);
CREATE INDEX idx_employee_profiles_experience ON employee_profiles(years_of_experience);

COMMENT ON TABLE employee_profiles IS 'Extended profile information for employees';
COMMENT ON COLUMN employee_profiles.availability IS 'Current availability status (AVAILABLE, PARTIALLY_AVAILABLE, UNAVAILABLE)';
COMMENT ON COLUMN employee_profiles.bio IS 'Short biography, max 500 characters';

-- Employee skills junction table - many-to-many with metadata
CREATE TABLE employee_skills (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 employee_id UUID NOT NULL,
                                 skill_id UUID NOT NULL,
                                 proficiency_score INTEGER NOT NULL,
                                 years_of_experience NUMERIC(3,1) NOT NULL DEFAULT 0.0,
                                 last_used DATE,
                                 CONSTRAINT fk_employee_skill_employee FOREIGN KEY (employee_id)
                                     REFERENCES employee_profiles(user_id) ON DELETE CASCADE,
                                 CONSTRAINT fk_employee_skill_skill FOREIGN KEY (skill_id)
                                     REFERENCES skills(id) ON DELETE CASCADE,
                                 CONSTRAINT uk_employee_skill UNIQUE (employee_id, skill_id),
                                 CONSTRAINT chk_proficiency_score CHECK (proficiency_score >= 0 AND proficiency_score <= 100),
                                 CONSTRAINT chk_skill_years_experience CHECK (years_of_experience >= 0)
);

CREATE INDEX idx_employee_skills_employee ON employee_skills(employee_id);
CREATE INDEX idx_employee_skills_skill ON employee_skills(skill_id);
CREATE INDEX idx_employee_skills_skill_proficiency ON employee_skills(skill_id, proficiency_score DESC);

COMMENT ON TABLE employee_skills IS 'Junction table linking employees to skills with proficiency metadata';
COMMENT ON COLUMN employee_skills.proficiency_score IS 'Skill proficiency from 0-100';
COMMENT ON COLUMN employee_skills.years_of_experience IS 'Years of experience with this specific skill';
COMMENT ON COLUMN employee_skills.last_used IS 'Date when this skill was last used in a project';
