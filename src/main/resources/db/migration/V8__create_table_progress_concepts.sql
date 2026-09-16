CREATE TABLE progress_concepts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    concept_id UUID NOT NULL REFERENCES concepts(id),
    covered BOOLEAN NOT NULL DEFAULT false,
    related_tasks INTEGER NOT NULL DEFAULT 0,
    UNIQUE(user_id, concept_id)
);