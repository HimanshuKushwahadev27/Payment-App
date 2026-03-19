CREATE TABLE wallet_balance (
    account_id UUID PRIMARY KEY,
    balance NUMERIC(19,2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);