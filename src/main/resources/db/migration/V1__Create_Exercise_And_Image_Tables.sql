-- ==============================================================================
-- V1: Utworzenie tabel dla katalogu ćwiczeń (exercise) oraz obrazków/animacji GIF (exercise_image)
-- ==============================================================================

-- 1. Tabela z definicjami ćwiczeń (metadane, partie mięśniowe, sprzęt, instrukcje)
CREATE TABLE exercise (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(150) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    primary_body_part VARCHAR(100),
    primary_equipment VARCHAR(100),
    primary_muscle VARCHAR(100),
    body_parts JSONB NOT NULL DEFAULT '[]'::jsonb,
    equipments JSONB NOT NULL DEFAULT '[]'::jsonb,
    target_muscles JSONB NOT NULL DEFAULT '[]'::jsonb,
    secondary_muscles JSONB NOT NULL DEFAULT '[]'::jsonb,
    instructions JSONB NOT NULL DEFAULT '[]'::jsonb,
    original_gif_url TEXT,
    image_file_name VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indeksy przyspieszające wyszukiwanie ćwiczeń w katalogu
CREATE INDEX idx_exercise_name_lower ON exercise (LOWER(name));
CREATE INDEX idx_exercise_primary_body_part ON exercise (primary_body_part);
CREATE INDEX idx_exercise_primary_equipment ON exercise (primary_equipment);
CREATE INDEX idx_exercise_primary_muscle ON exercise (primary_muscle);


-- 2. Tabela z binarnymi obrazkami / animacjami GIF (oddzielona od metadanych dla wydajności zapytań)
CREATE TABLE exercise_image (
    id BIGSERIAL PRIMARY KEY,
    exercise_id BIGINT NOT NULL UNIQUE REFERENCES exercise(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL DEFAULT 'image/gif',
    file_size_bytes BIGINT NOT NULL,
    image_data BYTEA NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indeks po nazwie pliku GIF
CREATE INDEX idx_exercise_image_file_name ON exercise_image (file_name);
