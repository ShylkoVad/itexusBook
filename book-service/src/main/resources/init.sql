-- Создание базы данных (если она еще не существует)
CREATE DATABASE IF NOT EXISTS Books_library_books;

-- Выбор базы данных
USE Books_library_books;

-- Создание таблицы books
CREATE TABLE books (
                      id SERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description VARCHAR(5000),
                      published_date DATE,
                      genre_id INT,
                      image_id VARCHAR(255) DEFAULT NULL  -- Добавляем поле для хранения ID изображения в GridFS, по умолчанию NULL
);

-- Создание таблицы book_authors (Связь между книгами и авторами)
CREATE TABLE book_authors (
                      book_id INT REFERENCES books(id) ON DELETE CASCADE,
                      author_id INT,
                      PRIMARY KEY (book_id, author_id)
);

-- Вставка книг
INSERT INTO books (title, description, published_date, genre_id, image_id) VALUES ('Мастер и маргарита',
                                                               'Загадочное и остроумное «Евангелие от Сатаны». ' ||
                                                               'Роман, уникальный в российской литературе ХХ столетия.',
                                                               '2022-11-11', 1, NULL);
INSERT INTO books (title, description, published_date, genre_id, image_id) VALUES ('Колосья под серпом твоим',
                                                               'В романе создана широкая панорама жизни народа ' ||
                                                               'в переломный для Беларуси период.',
                                                               '2024-02-04', 1, NULL);

-- Вставка связей между книгами и авторами
INSERT INTO book_authors (book_id, author_id) VALUES (1, 1); -- Книга 1 с Автором 1
INSERT INTO book_authors (book_id, author_id) VALUES (1, 2); -- Книга 1 с Автором 1
INSERT INTO book_authors (book_id, author_id) VALUES (2, 2); -- Книга 2 с Автором 2