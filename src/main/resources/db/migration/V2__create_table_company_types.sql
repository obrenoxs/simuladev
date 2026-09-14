CREATE TABLE company_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    domain_name VARCHAR(250) UNIQUE NOT NULL,
    description TEXT NOT NULL
);