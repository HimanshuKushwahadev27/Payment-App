CREATE TABLE idempotency_records (
    id UUID PRIMARY KEY,
    idempotency_key UUID NOT NULL,
    user_keycloak_id UUID NOT NULL,
    request_hash TEXT,
    response_body TEXT,
    http_status INT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    expires_at TIMESTAMP
);


CREATE UNIQUE INDEX idx_idempotency_key 
ON idempotency_records(idempotency_key);

CREATE INDEX idx_idempotency_user 
ON idempotency_records(user_keycloak_id);

CREATE INDEX idx_idempotency_expires 
ON idempotency_records(expires_at);