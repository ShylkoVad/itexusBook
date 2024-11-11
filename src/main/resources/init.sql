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
                       description VARCHAR(5000),
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

-- Вставка авторов
INSERT INTO authors (name, surname, birth_date) VALUES ('Михаил', 'Булгаков', '2000-01-01');
INSERT INTO authors (name, surname, birth_date) VALUES ('В.С.', 'Короткевич', '2002-02-02');

-- Вставка жанров
INSERT INTO genres (name) VALUES ('Роман');
INSERT INTO genres (name) VALUES ('Стихи');
INSERT INTO genres (name) VALUES ('Фантастика');

-- Вставка книг
INSERT INTO books (title, description, published_date) VALUES ('Мастер и маргарита',
                                                               'Загадочное и остроумное «Евангелие от Сатаны». ' ||
                                                               'Роман, уникальный в российской литературе ХХ столетия.',
                                                               '2022-11-11');
INSERT INTO books (title, description, published_date) VALUES ('Колосья под серпом твоим',
                                                               'В романе создана широкая панорама жизни народа ' ||
                                                               'в переломный для Беларуси период.',
                                                               '2024-02-04');

-- Вставка связей между книгами и авторами
INSERT INTO book_authors (book_id, author_id) VALUES (1, 1); -- Книга 1 с Автором 1
INSERT INTO book_authors (book_id, author_id) VALUES (2, 2); -- Книга 2 с Автором 2

-- Вставка связей между книгами и жанрами
INSERT INTO book_genres (book_id, genre_id) VALUES (1, 1); -- Книга 1 с Жанром 1
INSERT INTO book_genres (book_id, genre_id) VALUES (2, 1); -- Книга 2 с Жанром 1
