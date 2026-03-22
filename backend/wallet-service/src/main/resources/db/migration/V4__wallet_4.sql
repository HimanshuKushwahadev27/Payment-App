CREATE TABLE user_payout_account (
    id UUID PRIMARY KEY,
    user_keycloak_id UUID NOT NULL,
    destination_account_id VARCHAR(255) NOT NULL,
    bank_name VARCHAR(255),
    last4 VARCHAR(10),
    is_default BOOLEAN,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

ALTER TABLE user_payout_account
DROP CONSTRAINT IF EXISTS user_payout_account_destination_account_id_key;

CREATE UNIQUE INDEX uk_user_destination_account
ON user_payout_account(user_keycloak_id, destination_account_id);


CREATE INDEX idx_payout_user 
ON user_payout_account(user_keycloak_id);

CREATE INDEX idx_payout_default 
ON user_payout_account(user_keycloak_id, is_default);