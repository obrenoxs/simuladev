CREATE TABLE company_links (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_name VARCHAR(250) NOT NULL,
    start_date DATE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    user_id UUID NOT NULL REFERENCES users(id),
    company_type_id UUID NOT NULL REFERENCES company_types(id)
);