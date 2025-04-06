-- Создание базы данных (если она еще не существует)
CREATE DATABASE IF NOT EXISTS Books_library_authors;

-- Выбор базы данных
USE Books_library_authors;

-- Создание таблицы authors
CREATE TABLE authors (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       surname VARCHAR(255) NOT NULL,
                       birth_date DATE,
                       UNIQUE (name, surname)  -- Уникальное ограничение на сочетание имени и фамилии
);

-- Вставка авторов
INSERT INTO authors (name, surname, birth_date) VALUES ('Михаил', 'Булгаков', '2000-01-01');
INSERT INTO authors (name, surname, birth_date) VALUES ('В.С.', 'Короткевич', '2002-02-02');