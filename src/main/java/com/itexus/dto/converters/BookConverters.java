package com.itexus.dto.converters;

import com.itexus.domain.Book;
import com.itexus.dto.BookDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookConverters {

    private final GenreConverters genreConverters;
    private final AuthorConverters authorConverters;

    public BookConverters(GenreConverters genreConverters, AuthorConverters authorConverters) {
        this.genreConverters = genreConverters;
        this.authorConverters = authorConverters;
    }

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
                        .build())
                .orElse(null);
    }
}

