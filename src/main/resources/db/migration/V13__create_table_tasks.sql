CREATE TABLE tasks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_link_id UUID NOT NULL REFERENCES company_links(id),
    concept_id UUID NOT NULL REFERENCES concepts(id),
    type VARCHAR(8) NOT NULL,
    difficulty VARCHAR(6) NOT NULL,
    status VARCHAR(9) NOT NULL,
    ticket_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    delivered_at TIMESTAMP
);