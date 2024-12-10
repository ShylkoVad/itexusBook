package com.itexus.repository;

import com.itexus.domain.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreRepository {

    @Autowired
    private SessionFactory sessionFactory;

    // Метод для получения жанров по id
    public Genre findById(Long id) {
        Genre genre;
        Session session = sessionFactory.openSession();
        genre = session.get(Genre.class, id); // Получаем объект Genre по ID
        return genre; // Вернуть найденный
    }

    // Метод для получения всех жанров
    public List<Genre> findAll() {
        List<Genre> genres;
        Session session = sessionFactory.openSession();
        Query<Genre> query = session.createQuery("FROM Genre", Genre.class); // Создаем запрос для получения всех жанров
        genres = query.list(); // Получаем список жанров
        return genres; // Возвращаем список жанров
    }

    // Метод для сохранения жанра
    public void save(Genre genre) {
        Transaction transaction;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.persist(genre); // Сохраняем объект Genre в базе данных
        transaction.commit();
    }

    // Метод удаления жанра
    public void delete(Long id) {
        Transaction transaction;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        // Загружаем объект Genre по его ID
        Genre genre = session.get(Genre.class, id);
        if (genre != null) { // Проверяем, найден ли жанр
            session.remove(genre); // Удаляем жанр
            transaction.commit();
        } else {
            System.out.println("Genre with ID " + id + " not found."); // Информируем если не найден
        }
    }
}