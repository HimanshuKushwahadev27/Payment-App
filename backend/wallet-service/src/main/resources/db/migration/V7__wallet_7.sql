DELETE TABLE user_payout_account ;

CREATE TABLE user_payout_account(
    id UUID PRIMARY KEY,
    user_keycloak_id UUID NOT NULL,
    stripe_account_id UUID NOT NULL,
    charges_enabled BOOLEAN,
    payout_enabled  BOOLEAN,
    details_submitted BOOLEAN,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE UNIQUE INDEX uk_user_destination_account
ON user_payout_account(user_keycloak_id, stripe_account_id);

CREATE INDEX idx_payout_user 
ON user_payout_account(user_keycloak_id);
