CREATE TABLE user_info(
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	
	keycloak_id UUID NOT NULL UNIQUE,
	
    name VARCHAR(150) NOT NULL,

    email VARCHAR(255) NOT NULL UNIQUE,

    phone VARCHAR(15) UNIQUE,

    kyc_status VARCHAR(20),

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    is_deleted BOOLEAN DEFAULT FALSE
);

-- lookup user by keycloak id (very common)
CREATE INDEX idx_user_info_keycloak_id
ON user_info(keycloak_id);

-- search by email
CREATE INDEX idx_user_info_email
ON user_info(email);

-- search by phone
CREATE INDEX idx_user_info_phone
ON user_info(phone);

-- filtering by KYC status
CREATE INDEX idx_user_info_kyc_status
ON user_info(kyc_status);

-- filtering active users (soft delete)
CREATE INDEX idx_user_info_is_deleted
ON user_info(is_deleted);