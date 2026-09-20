#!/usr/bin/env bash
set -euo pipefail

# Configuration matching default values in application.properties
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-ptfitness-catalog}"
DB_USERNAME="${DB_USERNAME:-postgres}"
export PGPASSWORD="${DB_PASSWORD:-Pawel123}"

MODE="${1:---data}"

if [[ "$MODE" == "--schema" || "$MODE" == "--all" ]]; then
    echo "Dropping and recreating schema 'public' (including Flyway history) in database '$DB_NAME'..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USERNAME" -d "$DB_NAME" -v ON_ERROR_STOP=1 <<EOF
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO public;
EOF
    echo "Schema reset complete. Next './gradlew bootRun' will run Flyway migrations from scratch."
else
    echo "Truncating tables (exercise, exercise_image) in database '$DB_NAME'..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USERNAME" -d "$DB_NAME" -v ON_ERROR_STOP=1 <<EOF
TRUNCATE TABLE exercise_image, exercise RESTART IDENTITY CASCADE;
EOF
    echo "Table data cleared (ID sequences reset)."
fi
