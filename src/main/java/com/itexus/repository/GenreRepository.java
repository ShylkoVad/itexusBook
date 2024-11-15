package com.itexus.repository;

import com.itexus.domain.Genre;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    public GenreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(rs.getLong("id"));
        genre.setName(rs.getString("name"));
        return genre;
    };
    public Genre findById(Long id) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, genreRowMapper);
    }

    public List<Genre> findAll() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, genreRowMapper);
    }

    public void save(Genre genre) {
        String sql = "INSERT INTO genres (name) VALUES (?)";
        jdbcTemplate.update(sql, genre.getName());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM genres WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
