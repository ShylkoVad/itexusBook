package com.itexus.service;

import com.itexus.domain.Book;

import java.util.List;

public interface BookService {
    Book findById(Long id);

    List<Book> findAll();

    Long save(Book book);

    void update(Book book);

    void delete(Long id);
}
