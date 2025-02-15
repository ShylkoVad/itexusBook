package com.itexus.dto.converters;

import com.itexus.domain.Author;
import com.itexus.domain.Book;
import com.itexus.domain.Genre;
import com.itexus.dto.BookDTO;
import com.itexus.repository.AuthorRepository;
import com.itexus.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookConverters {

    private final GenreConverters genreConverters;
    private final AuthorConverters authorConverters;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookDTO toDTO(Book book) {
        return Optional.ofNullable(book).map(b -> BookDTO.builder()
                        .id(b.getId())
                        .title(b.getTitle())
                        .description(b.getDescription())
                        .publishedDate(b.getPublishedDate())
                        .genre(Optional.ofNullable(b.getGenre())
                                .map(genreConverters::toDTO) // Прямое преобразование жанра
                                .orElse(null)) // Если жанр отсутствует, возвращаем null
                        .authors(Optional.ofNullable(b.getAuthors())
                                .map(authors -> authors.stream()
                                        .map(authorConverters::toDTO)
                                        .toList()) // Здесь используется Collectors
                                .orElse(List.of())) // Если авторов нет, возвращаем пустой список
                        .imageId(b.getImageId())
                        .build())
                .orElse(null);
    }

    public Book fromDTO(BookDTO bookDTO) {
        return Optional.ofNullable(bookDTO).map(bd -> {
            // Получаем жанр по ID
            Genre genre = genreRepository.findById(bd.getGenre().getId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Жанра с id %d не найдено", bd.getGenre().getId())));
            // Получаем авторов по их ID
            Set<Author> authors = bd.getAuthors().stream()
                    .map(authorDTO -> authorRepository.findById(authorDTO.getId())
                            .orElseThrow(() -> new EntityNotFoundException(String.format("Автора с id %d не найдено", authorDTO.getId()))))
                    .collect(Collectors.toSet());
            // Создаем и возвращаем объект Book
            return Book.builder()
                    .title(bd.getTitle())
                    .description(bd.getDescription())
                    .publishedDate(bd.getPublishedDate())
                    .genre(genre)
                    .authors(authors)
                    .imageId(bd.getImageId())
                    .build();
        }).orElse(null);
    }
}

