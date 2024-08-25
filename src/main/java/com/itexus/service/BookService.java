package com.itexus.service;

import com.itexus.domain.Book;
import com.itexus.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public void saveBook (Book book) {
        bookRepository.save(book);
    }

    public void updateBook (Book book) {
        bookRepository.update(book);
    }

    public void deleteBook(Long id) {
        bookRepository.delete(id);
    }

    public Book findByIdBook (Long id) {
        return bookRepository.findById(id);
    }
}
