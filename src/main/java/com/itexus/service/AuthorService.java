package com.itexus.service;

import com.itexus.domain.Author;

import java.util.List;

public interface AuthorService {
    Author findById(Long id);

    List<Author> findAll();

    void save(Author author);

    void delete(Long id);

    List<Author> findAuthorsByBookId(Long bookId);
}
