CREATE TABLE project_states (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    state JSONB,
    company_link_id UUID UNIQUE NOT NULL REFERENCES company_links(id)
);