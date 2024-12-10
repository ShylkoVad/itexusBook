package com.itexus.repository;

import com.itexus.domain.Author;
import com.itexus.domain.Book;
import com.itexus.domain.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRepository {

    @Autowired
    private SessionFactory sessionFactory;

    // Метод для получения всех книг
    public List<Book> findAll() {
        Session session = sessionFactory.openSession();
        List<Book> book = session.createQuery("FROM Book", Book.class).list();
        return book;
    }

    // Метод для получения книги по id
    public Book findById(Long id) {
        Session session = sessionFactory.openSession();
        // Получение книги по ID
        Book book = session.get(Book.class, id);
        return book; // Возвращаем найденную книгу (null, если не найдена)
    }

    // Метод для сохранения книг
    public Long save(Book book) {
        Transaction transaction;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction(); // Начинаем транзакцию
        // Сохраняем объект книги
        session.persist(book); // Сохраняем книгу
        transaction.commit(); // Подтверждаем транзакцию
        // Получаем сгенерированный ID через геттер
        return book.getId(); // Возвращаем сгенерированный ID
    }

    // Метод обновления книг
    public void update(Book book) {
        Transaction transaction;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction(); // Начинаем транзакцию
        // Обновляем объект книги
        session.merge(book); // Используйте merge вместо update
        transaction.commit(); // Подтверждаем транзакцию
    }

    // Метод удаления книг
    public void delete(Long id) {
        Transaction transaction;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction(); // Начинаем транзакцию
        // Находим объект книги по ID
        Book book = session.get(Book.class, id);
        if (book != null) {
            session.remove(book); // Удаляем книгу
            transaction.commit(); // Подтверждаем транзакцию
        }
    }

    // Метод получения автора по id книги
    public void addAuthorToBook(Long bookId, Long authorId) {
        Transaction transaction;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction(); // Начинаем транзакцию
        // Загружаем книгу по ID
        Book book = session.get(Book.class, bookId);
        // Загружаем автора по ID
        Author author = session.get(Author.class, authorId);
        if (book != null && author != null) {
            // Добавляем автора к книге
            book.getAuthors().add(author); // Добавляем автора к книге
            author.getBooks().add(book); // Добавляем книгу к автору
            session.merge(book);  // Обновляем книгу
            session.merge(author); // Обновляем автора
        }
        transaction.commit(); // Подтверждаем транзакцию
    }

    // Метод получения жанра по id книги
    public void addGenreToBook(Long bookId, Long genreId) {
        Transaction transaction;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction(); // Начинаем транзакцию

        Book book = session.get(Book.class, bookId); // Загружаем книгу по ID
        Genre genre = session.get(Genre.class, genreId);  // Загружаем жанр по ID

        if (book != null && genre != null) {
            book.setGenre(genre); // Устанавливаем жанр для книги
            genre.getBooks().add(book); // Добавляем книгу к жанру
            session.merge(book); // Обновляем книгу
            session.merge(genre); // Обновляем жанр
        }
        transaction.commit(); // Подтверждаем транзакцию
    }
}
