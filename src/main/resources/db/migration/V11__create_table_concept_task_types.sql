CREATE TABLE concept_task_types (
    concept_id UUID NOT NULL REFERENCES concepts(id),
    task_type VARCHAR(20) NOT NULL,
    PRIMARY KEY(concept_id, task_type)
);