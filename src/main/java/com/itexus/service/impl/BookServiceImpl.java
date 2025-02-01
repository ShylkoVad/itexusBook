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
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import jakarta.persistence.EntityNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private final GridFSBucket gridFSBucket; // Для работы с GridFS

    public BookServiceImpl(BookRepository bookRepository, BookConverters bookConverters, AuthorRepository authorRepository,
                           GenreRepository genreRepository, GridFSBucket gridFSBucket) {
        this.bookRepository = bookRepository;
        this.bookConverters = bookConverters;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.gridFSBucket = gridFSBucket;
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

    // Реализация метода загрузки изображения
    @Override
    public String uploadImage(Long bookId, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Книга не найдена"));

        // Создаем ObjectId для файла
        ObjectId fileId = new ObjectId();

        // Сохраняем файл в GridFS с использованием fileId
        try (InputStream inputStream = file.getInputStream()) {
            gridFSBucket.uploadFromStream(fileId.toHexString(), inputStream);
        }

        // Сохраняем идентификатор файла (ObjectId) в объекте книги
        book.setImageId(fileId.toHexString()); // Сохраняем как строку, используя toHexString()
        bookRepository.save(book);

        return fileId.toHexString(); // Возвращаем идентификатор файла в виде строки
    }

    // Реализация метода получения изображения
    @Override
    public byte[] getImage(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Книга не найдена"));

        // Логируем найденную книгу
        System.out.println("Найдена книга: " + book.toString());

        String imageId = book.getImageId();
        if (imageId == null) {
            System.out.println("Изображение отсутствует для книги с id: " + bookId);
            return null; // Если изображения нет, возвращаем null
        }

        // Загружаем изображение из GridFS по filename
        GridFSFile gridFSFile = gridFSBucket.find(Filters.eq("filename", imageId)).first();
        if (gridFSFile == null) {
            System.out.println("Файл не найден в GridFS для filename: " + imageId);
            return null; // Если файл не найден, возвращаем null
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            gridFSBucket.downloadToStream(gridFSFile.getObjectId(), outputStream);
            return outputStream.toByteArray(); // Возвращаем данные изображения
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке изображения", e);
        }
    }

}
