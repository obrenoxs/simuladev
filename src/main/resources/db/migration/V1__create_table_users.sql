CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(250) NOT NULL,
    email  VARCHAR(254) UNIQUE NOT NULL,
    stack VARCHAR(250) NOT NULL,
    current_level VARCHAR(250) NOT NULL,
    level_percentage INTEGER NOT NULL,
    role VARCHAR(5) NOT NULL,
    password VARCHAR(60) NOT NULL
);