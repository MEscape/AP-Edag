-- V9__insert_initial_reference_data.sql
-- Populates reference tables with initial master data
-- Tables: skill_categories, skills, positions, locations
-- This data provides the foundation for employee profiles and project assignments

-- ===========================================================================
-- SKILL CATEGORIES
-- ===========================================================================
-- Hierarchical grouping of technical and soft skills

INSERT INTO skill_categories (id, name, is_active, created_at, updated_at) VALUES
                                                                               ('11111111-1111-1111-1111-111111111111', 'Frontend Development', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('22222222-2222-2222-2222-222222222222', 'Backend Development', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('33333333-3333-3333-3333-333333333333', 'Mobile Development', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('44444444-4444-4444-4444-444444444444', 'Database & Data Engineering', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('55555555-5555-5555-5555-555555555555', 'Cloud & Infrastructure', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('66666666-6666-6666-6666-666666666666', 'DevOps & CI/CD', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('77777777-7777-7777-7777-777777777777', 'Architecture & Design', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('88888888-8888-8888-8888-888888888888', 'Testing & Quality Assurance', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('99999999-9999-9999-9999-999999999999', 'Monitoring & Observability', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Security & Authentication', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Project Management & Agile', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Machine Learning & AI', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                               ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Business Intelligence & Analytics', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

COMMENT ON TABLE skill_categories IS 'Master catalog of skill categories for organizational hierarchy';

-- ===========================================================================
-- SKILLS
-- ===========================================================================
-- Comprehensive skill catalog organized by category

-- Frontend Development Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'React', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Angular', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Vue.js', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Next.js', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Nuxt.js', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Svelte', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'TypeScript', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'JavaScript', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'HTML5', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'CSS3', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Tailwind CSS', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Bootstrap', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Material-UI', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Sass/SCSS', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Webpack', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Vite', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Redux', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'MobX', '11111111-1111-1111-1111-111111111111', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Backend Development Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'Java', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Spring Boot', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Spring Framework', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Node.js', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Express.js', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'NestJS', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Python', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Django', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'FastAPI', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Flask', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'C#', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), '.NET Core', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'ASP.NET', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Go', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Rust', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'PHP', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Laravel', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Ruby on Rails', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'GraphQL', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'REST API', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'gRPC', '22222222-2222-2222-2222-222222222222', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Mobile Development Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'React Native', '33333333-3333-3333-3333-333333333333', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Flutter', '33333333-3333-3333-3333-333333333333', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Swift', '33333333-3333-3333-3333-333333333333', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'SwiftUI', '33333333-3333-3333-3333-333333333333', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Kotlin', '33333333-3333-3333-3333-333333333333', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Android SDK', '33333333-3333-3333-3333-333333333333', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Jetpack Compose', '33333333-3333-3333-3333-333333333333', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Xamarin', '33333333-3333-3333-3333-333333333333', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Ionic', '33333333-3333-3333-3333-333333333333', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Database & Data Engineering Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'PostgreSQL', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'MySQL', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'MongoDB', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Redis', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Elasticsearch', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Cassandra', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Oracle Database', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Microsoft SQL Server', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'DynamoDB', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Neo4j', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Apache Kafka', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Apache Spark', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Apache Airflow', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Snowflake', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'BigQuery', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Redshift', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'SQL', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'NoSQL', '44444444-4444-4444-4444-444444444444', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Cloud & Infrastructure Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'AWS', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'AWS Lambda', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'AWS EC2', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'AWS S3', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Azure', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Azure DevOps', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Google Cloud Platform', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'GCP Compute Engine', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Terraform', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Ansible', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'CloudFormation', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Pulumi', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Serverless Framework', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'DigitalOcean', '55555555-5555-5555-5555-555555555555', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- DevOps & CI/CD Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'Docker', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Kubernetes', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Helm', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Jenkins', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'GitHub Actions', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'GitLab CI/CD', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'CircleCI', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Travis CI', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'ArgoCD', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Spinnaker', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Nginx', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Apache', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Git', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'GitOps', '66666666-6666-6666-6666-666666666666', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Architecture & Design Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'Microservices Architecture', '77777777-7777-7777-7777-777777777777', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Event-Driven Architecture', '77777777-7777-7777-7777-777777777777', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Domain-Driven Design', '77777777-7777-7777-7777-777777777777', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'System Design', '77777777-7777-7777-7777-777777777777', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'API Design', '77777777-7777-7777-7777-777777777777', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Clean Architecture', '77777777-7777-7777-7777-777777777777', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'CQRS', '77777777-7777-7777-7777-777777777777', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Hexagonal Architecture', '77777777-7777-7777-7777-777777777777', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Design Patterns', '77777777-7777-7777-7777-777777777777', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'UML', '77777777-7777-7777-7777-777777777777', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Testing & Quality Assurance Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'JUnit', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Mockito', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Jest', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Cypress', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Playwright', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Selenium', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'TestNG', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Postman', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'SoapUI', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'K6', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'JMeter', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Cucumber', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'SonarQube', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Test-Driven Development', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Behavior-Driven Development', '88888888-8888-8888-8888-888888888888', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Monitoring & Observability Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'Grafana', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Prometheus', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Elastic Stack (ELK)', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Splunk', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Datadog', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'New Relic', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Dynatrace', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Jaeger', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Zipkin', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'OpenTelemetry', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'CloudWatch', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Kibana', '99999999-9999-9999-9999-999999999999', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Security & Authentication Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'OAuth 2.0', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'JWT', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'SAML', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Keycloak', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Auth0', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'OpenID Connect', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'OWASP Top 10', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Penetration Testing', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'SSL/TLS', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Encryption', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Vault (HashiCorp)', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Security Scanning', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Snyk', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'CSRF Protection', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'XSS Prevention', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Project Management & Agile Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'Agile Methodologies', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Scrum', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Kanban', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'SAFe', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Jira', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Confluence', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Azure Boards', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Monday.com', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Asana', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Trello', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Product Management', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Stakeholder Management', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Sprint Planning', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Backlog Management', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Machine Learning & AI Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'TensorFlow', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'PyTorch', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Scikit-learn', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Keras', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Pandas', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'NumPy', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'OpenCV', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Natural Language Processing', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Computer Vision', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Deep Learning', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'LangChain', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'LLM Integration', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Hugging Face', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'MLflow', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Jupyter Notebook', 'cccccccc-cccc-cccc-cccc-cccccccccccc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Business Intelligence & Analytics Skills
INSERT INTO skills (id, name, category_id, is_active, created_at, updated_at) VALUES
                                                                                  (gen_random_uuid(), 'Tableau', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Power BI', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Looker', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Metabase', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Apache Superset', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Data Modeling', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'ETL Pipelines', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Data Warehousing', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'dbt', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Excel', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'R Programming', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (gen_random_uuid(), 'Statistical Analysis', 'dddddddd-dddd-dddd-dddd-dddddddddddd', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

COMMENT ON TABLE skills IS 'Comprehensive master catalog of technical and professional skills';

-- ===========================================================================
-- POSITIONS
-- ===========================================================================
-- Job titles and roles within the organization

INSERT INTO positions (id, name, is_active, created_at, updated_at) VALUES
                                                                        ('10000000-0000-0000-0000-000000000001', 'Software Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000002', 'Senior Software Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000003', 'Lead Software Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000004', 'Principal Software Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000005', 'Staff Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000006', 'Frontend Developer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000007', 'Senior Frontend Developer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000008', 'Backend Developer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000009', 'Senior Backend Developer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000010', 'Full Stack Developer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000011', 'Senior Full Stack Developer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000012', 'Mobile Developer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000013', 'Senior Mobile Developer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000014', 'DevOps Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000015', 'Senior DevOps Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000016', 'Cloud Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000017', 'Senior Cloud Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000018', 'Cloud Architect', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000019', 'Solutions Architect', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000020', 'Enterprise Architect', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000021', 'Data Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000022', 'Senior Data Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000023', 'Data Scientist', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000024', 'Senior Data Scientist', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000025', 'ML Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000026', 'Senior ML Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000027', 'QA Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000028', 'Senior QA Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000029', 'Test Automation Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000030', 'Security Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000031', 'Senior Security Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000032', 'Security Architect', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000033', 'Database Administrator', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000034', 'Senior Database Administrator', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000035', 'Site Reliability Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000036', 'Senior Site Reliability Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000037', 'Engineering Manager', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000038', 'Senior Engineering Manager', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000039', 'Director of Engineering', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000040', 'VP of Engineering', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000041', 'CTO', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000042', 'Product Manager', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000043', 'Senior Product Manager', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000044', 'Technical Product Manager', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000045', 'Scrum Master', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000046', 'Agile Coach', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000047', 'Business Analyst', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000048', 'Senior Business Analyst', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000049', 'UX Designer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000050', 'Senior UX Designer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000051', 'UI Designer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000052', 'UX/UI Designer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000053', 'Technical Writer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000054', 'Platform Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('10000000-0000-0000-0000-000000000055', 'Infrastructure Engineer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

COMMENT ON TABLE positions IS 'Master catalog of organizational positions and job titles';

-- ===========================================================================
-- LOCATIONS
-- ===========================================================================
-- Office locations and work sites

INSERT INTO locations (id, name, is_active, created_at, updated_at) VALUES
                                                                        ('20000000-0000-0000-0000-000000000001', 'Berlin', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000002', 'München', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000003', 'Hamburg', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000004', 'Frankfurt', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000005', 'Köln', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000006', 'Stuttgart', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000007', 'Düsseldorf', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000008', 'Leipzig', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000009', 'Dresden', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000010', 'Nürnberg', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000011', 'Hannover', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000012', 'Bremen', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000013', 'Dortmund', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000014', 'Essen', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000015', 'Fulda', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000016', 'Böblingen', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000017', 'Lindau', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000018', 'Wolfsburg', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000019', 'Göttingen', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000020', 'Ingolstadt', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000021', 'Regensburg', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000022', 'Ulm', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000023', 'Karlsruhe', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000024', 'Mannheim', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000025', 'Augsburg', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000026', 'Wiesbaden', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000027', 'Münster', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000028', 'Bonn', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000029', 'Bielefeld', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                        ('20000000-0000-0000-0000-000000000030', 'Remote', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

COMMENT ON TABLE locations IS 'Geographic locations for employee assignments and project work';
