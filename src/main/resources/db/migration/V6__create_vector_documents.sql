CREATE TABLE vector_documents(
    id bigint primary key,
    incident_id bigint not null,
    chunk_index not null,
    content text not null,
    embedding not null,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP
)