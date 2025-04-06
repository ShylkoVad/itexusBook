package com.itexus.book.service.impl;

import com.itexus.author.dto.AuthorDTO;
import com.itexus.book.domain.Book;
import com.itexus.book.dto.BookDTO;
import com.itexus.book.dto.converters.BookConverters;
import com.itexus.book.repository.BookRepository;
import com.itexus.book.service.BookService;
import com.itexus.genre.dto.GenreDTO;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookConverters bookConverters;
    private final GridFSBucket gridFSBucket; // Для работы с GridFS
    private final WebClient webClient;

    @Override
    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll().stream().map(book -> {
            BookDTO bookDTO = bookConverters.toDTO(book);

            // Получаем идентификаторы авторов
            List<Long> authorIds = getAuthorIdsByBookId(book.getId());
            log.info("Author IDs for book ID {}: {}", book.getId(), authorIds);

            // Получаем информацию о жанре
            GenreDTO genreDTO = fetchGenre(book.getGenreId());
            bookDTO.setGenre(genreDTO);

            // Получаем информацию об авторах
            List<AuthorDTO> detailedAuthors = fetchAuthors(authorIds);
            bookDTO.setAuthors(detailedAuthors);

            return bookDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public BookDTO findByIdBook(Long id) {
        // Получаем книгу по ID, выбрасываем исключение, если не найдено
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Книги с id %d не найдено.", id)));

        // Преобразуем книгу в BookDTO
        BookDTO bookDTO = bookConverters.toDTO(book);

        // Получаем идентификаторы авторов
        List<Long> authorIds = getAuthorIdsByBookId(book.getId());
        log.info("Author IDs for book ID {}: {}", book.getId(), authorIds);

        // Получаем информацию о жанре
        GenreDTO genreDTO = fetchGenre(book.getGenreId());
        bookDTO.setGenre(genreDTO);

        // Получаем информацию об авторах
        List<AuthorDTO> detailedAuthors = fetchAuthors(authorIds);
        bookDTO.setAuthors(detailedAuthors);

        return bookDTO; // Возвращаем BookDTO
    }

    private GenreDTO fetchGenre(Long genreId) {
        return webClient.get()
                .uri("/genres/{id}", genreId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Client error: " + response.statusCode() + " - " + errorBody))))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Server error: " + response.statusCode() + " - " + errorBody))))
                .bodyToMono(GenreDTO.class)
                .block();
    }

    private List<AuthorDTO> fetchAuthors(List<Long> authorIds) {
        return authorIds.stream()
                .map(authorId -> {
                    try {
                        return webClient.get()
                                .uri("/authors/{id}", authorId)
                                .retrieve()
                                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                                    return response.bodyToMono(String.class)
                                            .flatMap(errorBody -> Mono.error(new RuntimeException("Client error: " + response.statusCode() + " - " + errorBody)));
                                })
                                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                                    return response.bodyToMono(String.class)
                                            .flatMap(errorBody -> Mono.error(new RuntimeException("Server error: " + response.statusCode() + " - " + errorBody)));
                                })
                                .bodyToMono(AuthorDTO.class)
                                .block(); // Блокируем для получения результата
                    } catch (Exception e) {
                        log.error("Error fetching author with id {}", authorId, e);
                        return null; // Или можно вернуть пустой AuthorDTO
                    }
                })
                .filter(Objects::nonNull) // Убираем null значения
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO saveBook(BookDTO bookDTO) {
        // Преобразуем BookDTO в объект Book
        Book book = bookConverters.fromDTO(bookDTO);

        // Сохраняем книгу в репозитории
        book = bookRepository.save(book); // Hibernate автоматически сохранит связи в book_authors

        // Преобразуем сохраненную книгу обратно в BookDTO и возвращаем
        return bookConverters.toDTO(book);
    }

    // Метод для сохранения связи книги с несколькими авторами
    public void saveBookAuthors(Long bookId, List<AuthorDTO> authors) {
        for (AuthorDTO author : authors) {
            saveBookAuthor(bookId, author.getId());
        }
    }

    @Transactional
    public BookDTO updateBook(BookDTO bookDTO) {
        // Находим книгу по ID
        Book book = bookRepository.findById(bookDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Книги с id %d не найдено.", bookDTO.getId())));

        // Обновляем поля книги
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublishedDate(bookDTO.getPublishedDate());
//        book.setGenreId(bookDTO.getGenre());

        // Сохраняем обновленную книгу
        Book updatedBook = bookRepository.save(book);

        // Сначала удаляем старые связи с авторами, если это необходимо
        bookRepository.removeBookAuthors(updatedBook.getId());

        // Затем добавляем новые связи с авторами
        for (AuthorDTO authorId : bookDTO.getAuthors()) {
            saveBookAuthor(updatedBook.getId(), authorId.getId());
        }

        // Возвращаем DTO обновленной книги
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

    @Override
    public List<Long> getAuthorIdsByBookId(Long bookId) {
        return bookRepository.findAuthorIdsByBookId(bookId);
    }


    public void updateBookAuthor(Long bookId, Long authorId, String newRole) {
        if (bookAuthorExists(bookId, authorId)) {
            bookRepository.updateBookAuthor(bookId, authorId, newRole);
        } else {
            throw new EntityNotFoundException("Запись не найдена для обновления.");
        }
    }

    private boolean bookAuthorExists(Long bookId, Long authorId) {
        return bookRepository.existsByBookIdAndAuthorId(bookId, authorId);
    }

    // Метод для сохранения связи книги с одним автором
    public void saveBookAuthor(Long bookId, Long authorId) {
        if (!bookRepository.existsByBookIdAndAuthorId(bookId, authorId)) {
            bookRepository.addBookAuthor(bookId, authorId);
        } else {
            System.out.println("Связь между книгой и автором уже существует.");
            // Или выбросьте исключение, если это необходимо
        }
    }

}
