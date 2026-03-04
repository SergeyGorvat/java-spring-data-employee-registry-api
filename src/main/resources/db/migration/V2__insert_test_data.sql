INSERT INTO departments (name)
VALUES ('Разработка'),
       ('Отдел кадров'),
       ('Бухгалтерия');

INSERT INTO employees (first_name, last_name, position, salary, department_id)
VALUES ('Иван', 'Иванов', 'Backend-разработчик', 150000, 1),
       ('Мария', 'Сидорова', 'HR-менеджер', 90000, 2),
       ('Пётр', 'Петров', 'Бухгалтер', 100000, 3);