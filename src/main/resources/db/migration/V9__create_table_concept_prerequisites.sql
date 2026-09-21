CREATE TABLE concept_prerequisites (
    concept_id UUID NOT NULL REFERENCES concepts(id),
    prerequisite_id UUID NOT NULL REFERENCES concepts(id),
    PRIMARY KEY(concept_id, prerequisite_id)
);