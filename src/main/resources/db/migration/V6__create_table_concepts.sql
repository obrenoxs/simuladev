CREATE TABLE concepts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    stack VARCHAR(150) NOT NULL,
    category VARCHAR(150) NOT NULL,
    concept_name VARCHAR (250) NOT NULL,
    weight INTEGER NOT NULL,
    target_level VARCHAR (100) NOT NULL
);