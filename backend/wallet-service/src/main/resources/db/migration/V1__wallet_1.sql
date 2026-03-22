CREATE TABLE account (
    id UUID PRIMARY KEY,
    user_keycloak_id UUID NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_user_account_type 
    UNIQUE (user_keycloak_id, account_type)
);


CREATE INDEX idx_account_user ON account(user_keycloak_id);
CREATE INDEX idx_account_type ON account(account_type);