CREATE TABLE USERS(
    id bigserial primary key,
    username varchar(50) not null,
    password varchar(255) not null,
    email varchar(255) not null,
    created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
    role varchar(20),
    constraint uk_users_email unique(email),
    constraint uk_users_username unique(username)
);