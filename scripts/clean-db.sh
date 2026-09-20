#!/usr/bin/env bash
set -euo pipefail

# Konfiguracja zgodna z wartościami domyślnymi w application.properties
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-ptfitness-catalog}"
DB_USERNAME="${DB_USERNAME:-postgres}"
export PGPASSWORD="${DB_PASSWORD:-Pawel123}"

MODE="${1:---data}"

if [[ "$MODE" == "--schema" || "$MODE" == "--all" ]]; then
    echo "Całkowite czyszczenie schematu 'public' (wraz z historią Flyway) w bazie '$DB_NAME'..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USERNAME" -d "$DB_NAME" -v ON_ERROR_STOP=1 <<EOF
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO public;
EOF
    echo "Schemat został zresetowany. Kolejne uruchomienie './gradlew bootRun' wykona migracje Flyway od nowa."
else
    echo "Czyszczenie danych z tabel (exercise, exercise_image) w bazie '$DB_NAME'..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USERNAME" -d "$DB_NAME" -v ON_ERROR_STOP=1 <<EOF
TRUNCATE TABLE exercise_image, exercise RESTART IDENTITY CASCADE;
EOF
    echo "Dane zostały wyczyszczone (liczniki ID zresetowane)."
fi
