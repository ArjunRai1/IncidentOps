CREATE TABLE INCIDENTS(
    id bigserial primary key,
    title varchar(200) not null,
    description text not null,
    created_by bigint not null,
    status varchar(20) not null default 'OPEN',
    priority varchar(20) not null,
    assigned_to bigint null,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_incidents_created_by FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_incidents_assigned_to FOREIGN KEY (assigned_to) REFERENCES users(id)
);

CREATE INDEX idx_incidents_status ON incidents(status);
CREATE INDEX idx_incidents_priority ON incidents(priority);
CREATE INDEX idx_incidents_created_by ON incidents(created_by);
CREATE INDEX idx_incidents_assigned_to ON incidents(assigned_to);
CREATE INDEX idx_incidents_created_at ON incidents(created_at DESC);
CREATE INDEX idx_incidents_status_created_at ON incidents(status, created_at DESC);