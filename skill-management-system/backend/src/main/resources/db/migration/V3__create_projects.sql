-- V3__create_projects.sql
-- Creates projects table and project_skills junction table
-- Projects are employee-centric, tracking work history and technologies used

-- Projects table
CREATE TABLE projects (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          employee_id UUID NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          status VARCHAR(32) NOT NULL DEFAULT 'PLANNED',
                          start_date DATE NOT NULL,
                          end_date DATE,
                          position_id UUID NOT NULL,
                          client VARCHAR(255),
                          team_size INTEGER,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_project_employee FOREIGN KEY (employee_id)
                              REFERENCES employee_profiles(user_id) ON DELETE CASCADE,
                          CONSTRAINT fk_project_position FOREIGN KEY (position_id)
                              REFERENCES positions(id) ON DELETE RESTRICT,
                          CONSTRAINT chk_project_dates CHECK (end_date IS NULL OR end_date >= start_date),
                          CONSTRAINT chk_project_team_size CHECK (team_size IS NULL OR team_size > 0),
                          CONSTRAINT chk_description_length CHECK (LENGTH(description) <= 2000)
);

CREATE INDEX idx_projects_employee_start_date ON projects(employee_id, start_date DESC);
CREATE INDEX idx_projects_employee_status ON projects(employee_id, status);
CREATE INDEX idx_projects_start_date ON projects(start_date DESC);
CREATE INDEX idx_projects_status ON projects(status);

COMMENT ON TABLE projects IS 'Employee project history and assignments';
COMMENT ON COLUMN projects.status IS 'Project status: PLANNED, ACTIVE, ON_HOLD, COMPLETED, CANCELLED';
COMMENT ON COLUMN projects.position_id IS 'Role/position held by employee in this project';
COMMENT ON COLUMN projects.team_size IS 'Number of team members on this project';

-- Project skills junction table
CREATE TABLE project_skills (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                project_id UUID NOT NULL,
                                skill_id UUID NOT NULL,
                                CONSTRAINT fk_project_skill_project FOREIGN KEY (project_id)
                                    REFERENCES projects(id) ON DELETE CASCADE,
                                CONSTRAINT fk_project_skill_skill FOREIGN KEY (skill_id)
                                    REFERENCES skills(id) ON DELETE CASCADE,
                                CONSTRAINT uk_project_skill UNIQUE (project_id, skill_id)
);

CREATE INDEX idx_project_skills_project ON project_skills(project_id);
CREATE INDEX idx_project_skills_skill ON project_skills(skill_id);

COMMENT ON TABLE project_skills IS 'Technologies/skills used in specific projects';
