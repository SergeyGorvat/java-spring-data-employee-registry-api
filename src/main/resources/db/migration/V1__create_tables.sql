CREATE TABLE departments
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE employees
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(255)   NOT NULL,
    last_name     VARCHAR(255)   NOT NULL,
    position      VARCHAR(255)   NOT NULL,
    salary        NUMERIC(12, 2) NOT NULL,
    department_id BIGINT         NOT NULL REFERENCES departments (id)
);