CREATE TABLE payments (

    id UUID PRIMARY KEY,

    user_keycloak_id UUID NOT NULL,

    gateway_transaction_id VARCHAR(255),

    amount NUMERIC(19,2) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,
    
    payment_method VARCHAR(60),
    
    currency VARCHAR(10) NOT NULL
);

CREATE INDEX idx_payment_user ON payments(user_keycloak_id);
CREATE INDEX idx_payment_status ON payments(status);