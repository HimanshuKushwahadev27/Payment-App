CREATE TABLE kyc (
    id UUID PRIMARY KEY,

    keycloak_id UUID UNIQUE,

    aadhaar_number VARCHAR(12) UNIQUE,
    pan_number VARCHAR(10) UNIQUE,

    document_url TEXT,
    selfie_url TEXT,

    status VARCHAR(20),

    otp_verified BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE document (
    id UUID PRIMARY KEY,

    img_url TEXT,

    type VARCHAR(20),

    keycloak_id UUID NOT NULL
);