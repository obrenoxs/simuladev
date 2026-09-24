CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    hash_token VARCHAR(60) NOT NULL,
    expiration_date TIMESTAMP NOT NULL
);