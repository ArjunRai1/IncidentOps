ALTER TABLE users
    ADD COLUMN username VARCHAR(80) NOT NULL UNIQUE;

CREATE INDEX idx_users_username ON users(username);

CREATE TABLE auth_otp_challenges (
    id BIGSERIAL PRIMARY KEY,
    flow_type VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL,
    username VARCHAR(80) NOT NULL,
    full_name VARCHAR(120),
    password_hash VARCHAR(255),
    user_id BIGINT,
    otp_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    consumed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_auth_otp_challenges_flow
        CHECK (flow_type IN ('REGISTER', 'LOGIN')),
    CONSTRAINT fk_auth_otp_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_auth_otp_email_flow ON auth_otp_challenges(email, flow_type);
CREATE INDEX idx_auth_otp_username_flow ON auth_otp_challenges(username, flow_type);
