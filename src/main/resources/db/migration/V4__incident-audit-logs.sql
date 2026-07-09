CREATE TABLE incident_audit_logs(
    id bigserial primary key,
    incident_id bigint  not null,
    user_id bigint not null,
    action varchar(20) not null,
    description text,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
)