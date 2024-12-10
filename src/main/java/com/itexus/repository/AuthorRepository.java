package com.itexus.repository;

import com.itexus.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRepository {

    @Autowired
    private SessionFactory sessionFactory;


    // Метод для получения автора по id
    public Author findById(Long id) {
        // Проверяем, если идентификатор не null
        if (id == null) {
            return null;
        }
        Session session = sessionFactory.openSession();
        // Загружаем автора по ID
        return session.get(Author.class, id);
    }

    // Метод для получения всех авторов
    public List<Author> findAll() {
        Session session = sessionFactory.openSession();
        return session.createQuery("FROM Author", Author.class).list();
    }

    // Метод для сохранения жанра
    public void save(Author author) {
        Transaction transaction;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction(); // Начинаем транзакцию
        session.persist(author); // Сохраняем объект автора в базе данных
        transaction.commit(); // Подтверждаем транзакцию
    }

    // Метод удаления жанра
    public void delete(Long id) {
        Transaction transaction;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction(); // Начинаем транзакцию
        // Загружаем автор по его ID
        Author author = session.get(Author.class, id);
        if (author != null) { // Проверяем, найден ли автор
            session.remove(author); // Удаляем автора
            transaction.commit(); // Подтверждаем транзакцию
        } else {
            System.out.println("Author with ID " + id + " not found.");
        }
    }

    // Метод получения всех авторов по id книги
    public List<Author> findAuthorsByBookId(Long bookId) {
        if (bookId == null) {
            return Collections.emptyList(); // Возвращаем пустой список, если bookId равен null
        }
        Session session = sessionFactory.openSession();
        // Используем HQL для получения авторов по идентификатору книги
        String hql = "SELECT a FROM Author a JOIN a.books b WHERE b.id = :bookId";
        Query<Author> query = session.createQuery(hql, Author.class);
        query.setParameter("bookId", bookId);

        return query.getResultList(); // Возвращаем список авторов
    }
}
