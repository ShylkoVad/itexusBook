-- Создание таблицы authors
CREATE TABLE authors (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       surname VARCHAR(255) NOT NULL,
                       birth_date DATE
);

-- Создание таблицы genres
CREATE TABLE genres (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL
);

-- Создание таблицы books
CREATE TABLE books (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       published_date DATE
);

-- Создание таблицы book_authors (Связь между книгами и авторами)
CREATE TABLE book_authors (
                       book_id INT REFERENCES books(id),
                       author_id INT REFERENCES authors(id),
                       PRIMARY KEY (book_id, author_id)
);

-- Создание таблицы book_genres (Связь между книгами и жанрами)
CREATE TABLE book_genres (
                       book_id INT REFERENCES books(id),
                       genre_id INT REFERENCES genres(id),
                       PRIMARY KEY (book_id, genre_id)
);