package com.itexus.repository;

import com.itexus.domain.Author;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class AuthorRepository {
    private final JdbcTemplate jdbcTemplate;

    public AuthorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Author> authorRowMapper = (rs, rowNum) -> {
        Author author = new Author();
        author.setId(rs.getLong("id"));
        author.setName(rs.getString("name"));
        author.setSurname(rs.getString("surname"));
        author.setBirthDate(rs.getDate("birth_date").toLocalDate());
        return author;
    };

    public Author findById(Long id) {
        String sql = "SELECT * FROM authors WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, authorRowMapper);
    }

    public List<Author> findAll() {
        String sql = "SELECT * FROM authors";
        return jdbcTemplate.query(sql, authorRowMapper);
    }

    public void save(Author author) {
        String sql = "INSERT INTO authors (name, surname, birth_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, author.getName(), author.getSurname(), author.getBirthDate());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM authors WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
