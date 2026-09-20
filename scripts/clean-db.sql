-- ==============================================================================
-- Skrypt czyszczący bazę danych ptfitness-catalog
-- ==============================================================================

-- OPCJA 1: Wyczyszczenie samych danych (z zachowaniem struktur tabel i historii Flyway)
-- Resetuje liczniki BIGSERIAL (RESTART IDENTITY) i czyści powiązane rekordy (CASCADE).
TRUNCATE TABLE exercise_image, exercise RESTART IDENTITY CASCADE;

-- OPCJA 2: Całkowite wyczyszczenie schematu (wraz z tabelą flyway_schema_history)
-- Odkomentuj poniższe linie, jeśli chcesz, aby Flyway przy kolejnym uruchomieniu
-- aplikacji odtworzył wszystkie tabele od zera:
--
-- DROP SCHEMA public CASCADE;
-- CREATE SCHEMA public;
-- GRANT ALL ON SCHEMA public TO public;
