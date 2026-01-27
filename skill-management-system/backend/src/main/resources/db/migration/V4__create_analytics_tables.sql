-- V4__create_analytics_tables.sql
-- Creates analytics and audit tables: user_statistics, skill_development, activities
-- These tables support dashboards, reporting, and user activity tracking

-- User statistics table - aggregated metrics per user
CREATE TABLE user_statistics (
                                 user_id UUID PRIMARY KEY,
                                 total_skills INTEGER NOT NULL DEFAULT 0,
                                 total_projects INTEGER NOT NULL DEFAULT 0,
                                 total_recommendations INTEGER NOT NULL DEFAULT 0,
                                 active_projects INTEGER NOT NULL DEFAULT 0,
                                 average_skill_score NUMERIC(5,2) DEFAULT 0.0,
                                 last_calculated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_user_stats_user FOREIGN KEY (user_id)
                                     REFERENCES users(id) ON DELETE CASCADE,
                                 CONSTRAINT uk_user_statistics_user UNIQUE (user_id),
                                 CONSTRAINT chk_total_skills CHECK (total_skills >= 0),
                                 CONSTRAINT chk_total_projects CHECK (total_projects >= 0),
                                 CONSTRAINT chk_active_projects CHECK (active_projects >= 0 AND active_projects <= total_projects),
                                 CONSTRAINT chk_average_skill_score CHECK (average_skill_score IS NULL OR (average_skill_score >= 0 AND average_skill_score <= 100))
);

CREATE INDEX idx_user_stats_last_calculated ON user_statistics(last_calculated);

COMMENT ON TABLE user_statistics IS 'Aggregated statistics for user dashboards and analytics';
COMMENT ON COLUMN user_statistics.last_calculated IS 'Timestamp of last statistics recalculation';

-- Skill development table - monthly skill growth tracking
CREATE TABLE skill_development (
                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                   user_id UUID NOT NULL,
                                   month DATE NOT NULL,
                                   total_skills INTEGER NOT NULL DEFAULT 0,
                                   skills_added INTEGER NOT NULL DEFAULT 0,
                                   skills_updated INTEGER NOT NULL DEFAULT 0,
                                   skills_removed INTEGER NOT NULL DEFAULT 0,
                                   top_category VARCHAR(128),
                                   CONSTRAINT fk_skill_development_user FOREIGN KEY (user_id)
                                       REFERENCES users(id) ON DELETE CASCADE,
                                   CONSTRAINT uk_skill_development_user_month UNIQUE (user_id, month),
                                   CONSTRAINT chk_total_skills_dev CHECK (total_skills >= 0),
                                   CONSTRAINT chk_skills_added CHECK (skills_added >= 0),
                                   CONSTRAINT chk_skills_updated CHECK (skills_updated >= 0),
                                   CONSTRAINT chk_skills_removed CHECK (skills_removed >= 0)
);

CREATE INDEX idx_skill_development_user_month ON skill_development(user_id, month DESC);

COMMENT ON TABLE skill_development IS 'Monthly skill development metrics for tracking growth over time';
COMMENT ON COLUMN skill_development.month IS 'First day of the month for this snapshot';
COMMENT ON COLUMN skill_development.top_category IS 'Most prominent skill category for this month';

-- Activities table - audit log of user actions
CREATE TABLE activities (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            user_id UUID NOT NULL,
                            type VARCHAR(64) NOT NULL,
                            timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            entity_id UUID,
                            entity_type VARCHAR(50),
                            CONSTRAINT fk_activity_user FOREIGN KEY (user_id)
                                REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_activities_user_timestamp ON activities(user_id, timestamp DESC);
CREATE INDEX idx_activities_type ON activities(type);
CREATE INDEX idx_activities_entity ON activities(entity_type, entity_id);

-- Space optimization: More aggressive auto-vacuum for high-traffic table
ALTER TABLE activities SET (
    autovacuum_vacuum_scale_factor = 0.05,
    autovacuum_analyze_scale_factor = 0.02
);

COMMENT ON TABLE activities IS 'Immutable audit log of user activities for analytics and monitoring';
COMMENT ON COLUMN activities.type IS 'Activity type enum (e.g., CREATED_SKILL, UPDATED_PROFILE, COMPLETED_PROJECT)';
COMMENT ON COLUMN activities.entity_id IS 'Optional reference to related entity';
COMMENT ON COLUMN activities.entity_type IS 'Type of related entity (e.g., SKILL, PROJECT, PROFILE)';
