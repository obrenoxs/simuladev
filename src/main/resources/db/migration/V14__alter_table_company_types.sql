ALTER TABLE company_types
ADD COLUMN foundational_concept_id UUID REFERENCES concepts(id);