CREATE TABLE users
(
    id                 BIGSERIAL PRIMARY KEY,
    username           VARCHAR(255) NOT NULL UNIQUE,
    password           VARCHAR(255) NOT NULL,
    role               VARCHAR(50)  NOT NULL,
    account_non_locked BOOLEAN      NOT NULL DEFAULT TRUE,
    failed_attempts    INT          NOT NULL DEFAULT 0
);

INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$7QpyV3zMpBvHjxHZbhCBt.Y8VrFk3LJDaB7J5OKTlwOWmxJkqY2Iq', 'SUPER_ADMIN'),
       ('moderator', '$2a$10$7QpyV3zMpBvHjxHZbhCBt.Y8VrFk3LJDaB7J5OKTlwOWmxJkqY2Iq', 'MODERATOR'),
       ('user', '$2a$10$7QpyV3zMpBvHjxHZbhCBt.Y8VrFk3LJDaB7J5OKTlwOWmxJkqY2Iq', 'USER');
