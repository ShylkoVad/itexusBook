package com.itexus.service.impl;

import com.itexus.domain.Book;
import com.itexus.dto.BookDTO;
import com.itexus.dto.converters.BookConverters;
import com.itexus.repository.BookRepository;
import com.itexus.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookConverters bookConverters;

    public BookServiceImpl(BookRepository bookRepository, BookConverters bookConverters) {
        this.bookRepository = bookRepository;
        this.bookConverters = bookConverters;
    }

    @Override
    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll().stream().map(bookConverters::toDTO).toList();
    }

    @Override
    public BookDTO findByIdBook(Long id) {
        return bookConverters.toDTO(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Книги с id %d не найдено.", id))));
    }

    @Override
    public Long saveBook(BookDTO bookDTO) {
        // Реализация сохранения книги
        return null;
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO) {
        Book book = bookRepository.findById(bookDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Книги с id %d не найдено.", bookDTO.getId())));
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublishedDate(bookDTO.getPublishedDate());
        return bookConverters.toDTO(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Книги с id %d не найдено.", id)));
        bookRepository.delete(book);
    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        // Реализация добавления автора к книге
    }

}
