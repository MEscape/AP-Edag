#!/bin/bash
set -e

echo "Creating databases and users..."

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    SELECT 'CREATE DATABASE keycloak' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak')\gexec

    DO \$\$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'keycloak') THEN
            CREATE USER keycloak WITH PASSWORD '${KC_DB_PASSWORD}';
        END IF;
    END
    \$\$;

    GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;

    \c keycloak
    GRANT ALL ON SCHEMA public TO keycloak;
    GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO keycloak;
    GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO keycloak;
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO keycloak;
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO keycloak;
EOSQL

echo "Databases and users created successfully!"
