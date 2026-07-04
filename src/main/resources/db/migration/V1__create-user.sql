CREATE TABLE USERS(
    id bigserial primary key,
    username varchar(50) not null,
    password varchar(255) not null,
    email varchar(255) not null,
    createdAt timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updatedAt timestamp not null DEFAULT CURRENT_TIMESTAMP,
    roll varchar(20),
    constraint uk_users_email unique(email),
    constraint uk_users_username unique(username)
);