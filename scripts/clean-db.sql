-- ==============================================================================
-- Database cleanup script for ptfitness-catalog
-- ==============================================================================

-- OPTION 1: Clear table data only (preserving table structures and Flyway history)
-- Resets BIGSERIAL sequences (RESTART IDENTITY) and removes dependent rows (CASCADE).
TRUNCATE TABLE exercise_image, exercise RESTART IDENTITY CASCADE;

-- OPTION 2: Full schema reset (including flyway_schema_history table)
-- Uncomment the lines below if you want Flyway to recreate all tables from scratch
-- on the next application startup:
--
-- DROP SCHEMA public CASCADE;
-- CREATE SCHEMA public;
-- GRANT ALL ON SCHEMA public TO public;
