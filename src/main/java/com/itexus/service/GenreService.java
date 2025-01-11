package com.itexus.service;

import com.itexus.domain.Genre;

import java.util.List;

public interface GenreService {
    Genre findById(Long id);

    List<Genre> findAll();

    void save(Genre genre);

    void delete(Long id);
}
