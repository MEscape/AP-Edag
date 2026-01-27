-- V1__create_base_tables.sql
-- Creates foundational tables: users, positions, locations, skill_categories, skills
-- These are the master data tables that other entities will reference

-- Enable PostgreSQL extensions for full-text search
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Users table - central authentication/identity table
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(128) NOT NULL UNIQUE,
                       email VARCHAR(320) NOT NULL UNIQUE,
                       first_name VARCHAR(128),
                       last_name VARCHAR(128),
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_created_at ON users(created_at);

COMMENT ON TABLE users IS 'Central user identity table synchronized from Keycloak';
COMMENT ON COLUMN users.username IS 'Unique username from identity provider';
COMMENT ON COLUMN users.email IS 'Unique email address';

-- Positions table - job roles/titles
CREATE TABLE positions (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           name VARCHAR(128) NOT NULL UNIQUE,
                           is_active BOOLEAN NOT NULL DEFAULT true,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_positions_active ON positions(is_active);
CREATE INDEX idx_positions_name ON positions(name);

COMMENT ON TABLE positions IS 'Master catalog of job positions/roles';

-- Locations table - office/workplace locations
CREATE TABLE locations (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           name VARCHAR(128) NOT NULL UNIQUE,
                           is_active BOOLEAN NOT NULL DEFAULT true,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_locations_active ON locations(is_active);
CREATE INDEX idx_locations_name ON locations(name);

COMMENT ON TABLE locations IS 'Master catalog of workplace locations';

-- Skill categories table - grouping for skills
CREATE TABLE skill_categories (
                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                  name VARCHAR(128) NOT NULL UNIQUE,
                                  is_active BOOLEAN NOT NULL DEFAULT true,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_skill_categories_active ON skill_categories(is_active);
CREATE INDEX idx_skill_categories_name ON skill_categories(name);

COMMENT ON TABLE skill_categories IS 'Hierarchical grouping for skills (e.g., Backend, Frontend, DevOps)';

-- Skills table - master skill catalog
CREATE TABLE skills (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        name VARCHAR(128) NOT NULL UNIQUE,
                        category_id UUID NOT NULL,
                        is_active BOOLEAN NOT NULL DEFAULT true,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_skills_category FOREIGN KEY (category_id)
                            REFERENCES skill_categories(id) ON DELETE RESTRICT
);

CREATE INDEX idx_skills_category_active ON skills(category_id, is_active);

COMMENT ON TABLE skills IS 'Master catalog of all available skills/technologies';
COMMENT ON COLUMN skills.category_id IS 'Skill category for organization and filtering';
