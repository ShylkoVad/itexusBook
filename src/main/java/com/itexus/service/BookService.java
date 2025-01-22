package com.itexus.service;

import com.itexus.dto.BookDTO;

import java.util.List;

public interface BookService {
    List<BookDTO> findAllBooks();

    BookDTO findByIdBook(Long id);

    Long saveBook(BookDTO bookDTO);

    BookDTO updateBook(BookDTO bookDTO);

    void deleteBook(Long id);

    void addAuthorToBook(Long bookId, Long authorId);
}
