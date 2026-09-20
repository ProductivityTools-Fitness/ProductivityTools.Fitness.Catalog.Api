-- ==============================================================================
-- V2: Rename external_id column to exercise_id in exercise table
-- ==============================================================================

ALTER TABLE exercise RENAME COLUMN external_id TO exercise_id;

ALTER INDEX IF EXISTS exercise_external_id_key RENAME TO exercise_exercise_id_key;
