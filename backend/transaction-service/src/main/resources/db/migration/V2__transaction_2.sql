CREATE TABLE transactions (
    id UUID PRIMARY KEY,

    user_keycloak_id UUID NOT NULL,

    from_account_id VARCHAR(255),
    to_account_id VARCHAR(255),

    amount NUMERIC(19,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,

    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);


CREATE INDEX idx_tx_user_created 
ON transactions(user_keycloak_id, created_at DESC);

CREATE INDEX idx_tx_status 
ON transactions(status);


CREATE INDEX idx_tx_from_account 
ON transactions(from_account_id);

CREATE INDEX idx_tx_to_account 
ON transactions(to_account_id);