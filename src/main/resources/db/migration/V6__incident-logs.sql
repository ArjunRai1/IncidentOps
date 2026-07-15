CREATE TABLE incident_logs(
    id BIGSERIAL PRIMARY KEY,
    incident_id BIGINT NOT NULL,
    filename VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_incident_logs_incident FOREIGN KEY (incident_id) REFERENCES incidents(id) ON DELETE CASCADE
);

CREATE INDEX idx_incident_logs_incident ON incident_logs(incident_id);