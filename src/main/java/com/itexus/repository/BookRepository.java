package com.itexus.repository;

import com.itexus.domain.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();

    Book findById(Long id);

    void save(Book book);

    void update(Book book);

    void delete(Long id);
}
