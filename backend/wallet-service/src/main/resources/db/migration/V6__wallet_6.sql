CREATE TABLE ledger_entry (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL,
    to_account_id VARCHAR(255) NOT NULL,
    from_account_id VARCHAR(255) NOT NULL,
    entry_type VARCHAR(50) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);


CREATE INDEX idx_ledger_transaction 
ON ledger_entry(transaction_id);

CREATE INDEX idx_ledger_to_account 
ON ledger_entry(to_account_id);

CREATE INDEX idx_ledger_from_account 
ON ledger_entry(from_account_id);

CREATE INDEX idx_ledger_account_time 
ON ledger_entry(to_account_id, created_at DESC);

CREATE INDEX idx_ledger_status 
ON ledger_entry(status);