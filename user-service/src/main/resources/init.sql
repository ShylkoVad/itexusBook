-- Создание базы данных (если она еще не существует)
CREATE DATABASE IF NOT EXISTS Books_library_users;

-- Выбор базы данных
USE Books_library_users;

-- Создание таблицы users
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL
);

-- Создание таблицы roles
CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL
);

-- Создание таблицы users_roles
CREATE TABLE users_roles (
                             user_id INT NOT NULL,
                             role_id INT NOT NULL,
                             PRIMARY KEY (user_id, role_id),
                             FOREIGN KEY (user_id) REFERENCES users(id),
                             FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Создание таблицы tokens
CREATE TABLE tokens (
                        id SERIAL PRIMARY KEY,
                        token VARCHAR(255) NOT NULL,
                        user_id INT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Вставка ролей
INSERT INTO roles(name) VALUES ('ADMIN');
INSERT INTO roles(name) VALUES ('USER');