package com.itexus.repository;

import com.itexus.domain.Book;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class BookRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Book> bookRowMapper = (rs, rowNum) -> {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setDescription(rs.getString("description"));
        book.setPublishedDate(rs.getDate("published_date").toLocalDate());
        return book;
    };

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    public Book findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, bookRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null; // Возвращаем null, чтобы обработать это на уровне сервиса
        }
    }

    public Long save(Book book) {
        String sql = "INSERT INTO books (title, description, published_date) VALUES (?, ?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, new Object[]{book.getTitle(), book.getDescription(), book.getPublishedDate()}, Long.class);
    }

    public void update(Book book) {
        String sql = "UPDATE books SET title = ?, description = ?, published_date = ? WHERE id = ?";
        jdbcTemplate.update(sql, book.getTitle(), book.getDescription(), book.getPublishedDate(), book.getId());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void addAuthorToBook(Long bookId, Long authorId) {
        String sql = "INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, bookId, authorId);
    }

    public void addGenreToBook(Long bookId, Long genreId) {
        String sql = "INSERT INTO book_genres (book_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, bookId, genreId);
    }
}
