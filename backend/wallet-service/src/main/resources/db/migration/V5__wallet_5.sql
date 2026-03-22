CREATE TABLE processed_events (
    id UUID PRIMARY KEY,
    expires_at TIMESTAMP
);

CREATE INDEX idx_processed_expires 
ON processed_events(expires_at);