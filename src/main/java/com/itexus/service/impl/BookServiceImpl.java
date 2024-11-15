package com.itexus.service.impl;

import com.itexus.domain.Book;
import com.itexus.repository.BookRepository;
import com.itexus.service.BookService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Long save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void update(Book book) {
        bookRepository.update(book);
    }

    @Override
    public void delete(Long id) {

        bookRepository.delete(id);
    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        bookRepository.addAuthorToBook(bookId, authorId);
    }

    @Override
    public void addGenreToBook(Long bookId, Long genreId) {
        bookRepository.addGenreToBook(bookId, genreId);
    }
}
