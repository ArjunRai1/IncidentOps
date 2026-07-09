CREATE TABLE COMMENTS(
    id bigserial primary key,
    incident_id bigint not null,
    user_id bigint not null,
    comment text not null,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

    constraint fk_comments_user_id foreign key (user_id) references users(id),
    constraint fk_comments_incident_id foreign key (incident_id) references incidents(id)
);

CREATE INDEX idx_comments_incident_created_at ON COMMENTS (incident_id, created_at);