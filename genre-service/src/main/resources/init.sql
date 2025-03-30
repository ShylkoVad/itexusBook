-- Создание базы данных (если она еще не существует)
CREATE DATABASE IF NOT EXISTS Books_library_genres;

-- Выбор базы данных
USE Books_library_genres;

-- Создание таблицы genres
CREATE TABLE genres (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       UNIQUE (name)  -- Уникальное ограничение на имя жанра
);

-- Вставка жанров
INSERT INTO genres (name) VALUES ('Роман');
INSERT INTO genres (name) VALUES ('Стихи');
INSERT INTO genres (name) VALUES ('Фантастика');
