package com.itexus.service.impl;

import com.itexus.domain.Author;
import com.itexus.domain.Book;
import com.itexus.domain.Genre;
import com.itexus.dto.BookDTO;
import com.itexus.dto.converters.BookConverters;
import com.itexus.repository.AuthorRepository;
import com.itexus.repository.BookRepository;
import com.itexus.repository.GenreRepository;
import com.itexus.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookConverters bookConverters;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookServiceImpl(BookRepository bookRepository, BookConverters bookConverters, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.bookConverters = bookConverters;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll().stream().map(bookConverters::toDTO).collect(Collectors.toList());
    }

    @Override
    public BookDTO findByIdBook(Long id) {
        return bookConverters.toDTO(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Книги с id %d не найдено.", id))));
    }

    @Override
    public BookDTO saveBook(BookDTO bookDTO) {
        Book book = bookConverters.fromDTO(bookDTO);
        book = bookRepository.save(book);
        return bookConverters.toDTO(book);
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO) {
        // Находим книгу по ID
        Book book = bookRepository.findById(bookDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Книги с id %d не найдено.", bookDTO.getId())));

        // Обновляем поля книги
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublishedDate(bookDTO.getPublishedDate());

        // Загружаем жанр из базы данных
        if (bookDTO.getGenre() != null) {
            Genre genre = genreRepository.findById(bookDTO.getGenre().getId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Жанр с id %d не найден.", bookDTO.getGenre().getId())));
            book.setGenre(genre);
        }

        // Обновляем авторов
        if (bookDTO.getAuthors() != null) {
            Set<Author> updatedAuthors = bookDTO.getAuthors().stream()
                    .map(authorDTO -> authorRepository.findById(authorDTO.getId())
                            .orElseThrow(() -> new EntityNotFoundException(String.format("Автор с id %d не найден.", authorDTO.getId()))))
                    .collect(Collectors.toSet());
            book.setAuthors(updatedAuthors);
        }

        // Сохраняем обновленную книгу и преобразуем ее в BookDTO
        Book updatedBook = bookRepository.save(book);

        return bookConverters.toDTO(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Книги с id %d не найдено.", id)));
        bookRepository.delete(book);
    }
}
