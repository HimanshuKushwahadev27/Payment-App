CREATE TABLE ledger_entry (

    id UUID PRIMARY KEY,

    transaction_id UUID NOT NULL,

    account_id UUID NOT NULL,

    entry_type VARCHAR(10) NOT NULL,

    amount NUMERIC(19,2) NOT NULL,

    created_at TIMESTAMP NOT NULL
);

CREATE TABLE account (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_user_account_type
    UNIQUE(user_id, account_type)
    
    CONSTRAINT currency_format
    CHECK (currency ~ '^[A-Z]{3}$'),
);

CREATE TABLE wallet_balance (

    account_id UUID PRIMARY KEY,

    balance NUMERIC(19,2) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE idempotency_keys (

    id UUID PRIMARY KEY,

    idempotency_key VARCHAR(255) UNIQUE NOT NULL,

    request_hash VARCHAR(255),

    response_body TEXT,

    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_ledger_account
ON ledger_entry(account_id);

CREATE INDEX idx_ledger_transaction
ON ledger_entry(transaction_id);