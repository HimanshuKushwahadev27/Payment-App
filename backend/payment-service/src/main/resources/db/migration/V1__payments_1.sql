CREATE TABLE payments (

    id UUID PRIMARY KEY,

    user_keycloak_id UUID NOT NULL,

    gateway_transaction_id VARCHAR(255),

    payout_transaction_id VARCHAR(255),
    
    to_account_id VARCHAR(255),
    
    amount NUMERIC(19,2) NOT NULL,

    status VARCHAR(20) NOT NULL,

    payment_type VARCHAR(20),
    
    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,
    
    payment_method_type VARCHAR(60),
    
    currency VARCHAR(10) NOT NULL
);

CREATE INDEX idx_payment_user ON payments(user_keycloak_id);
CREATE INDEX idx_payment_status ON payments(status);