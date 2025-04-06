package com.itexus.book.dto.converters;

import com.itexus.author.domain.Author;
import com.itexus.author.dto.AuthorDTO;
import com.itexus.book.domain.Book;
import com.itexus.book.dto.BookDTO;
import com.itexus.genre.dto.GenreDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookConverters {

    public BookDTO toDTO(Book book) {
        return Optional.ofNullable(book).map(b -> {
            GenreDTO genreDTO = new GenreDTO();
            genreDTO.setId(b.getGenreId());

            // Преобразуем список авторов в список AuthorDTO
            List<AuthorDTO> authorDTOs = Optional.ofNullable(b.getBookAuthors()) // Убедитесь, что это не null
                    .orElse(List.of()) // Если null, возвращаем пустой список
                    .stream() // Начинаем стрим
                    .map(author -> new AuthorDTO(
                            author.getId(), // Убедитесь, что author - это объект типа Author
                            author.getName(),
                            author.getSurname(),
                            author.getBirthDate()
                    ))
                    .collect(Collectors.toList()); // Собираем в список

            return BookDTO.builder()
                    .id(b.getId())
                    .title(b.getTitle())
                    .description(b.getDescription())
                    .publishedDate(b.getPublishedDate())
                    .genre(genreDTO)
                    .authors(authorDTOs) // Устанавливаем список AuthorDTO
                    .imageId(b.getImageId())
                    .build();
        }).orElse(null);
    }

    public Book fromDTO(BookDTO bookDTO) {
        return Optional.ofNullable(bookDTO).map(bd -> {
            return Book.builder()
                    .id(bd.getId())
                    .title(bd.getTitle())
                    .description(bd.getDescription())
                    .publishedDate(bd.getPublishedDate())
                    .genreId(bd.getGenre() != null ? bd.getGenre().getId() : null)
                    .bookAuthors(Optional.ofNullable(bd.getAuthors()).orElse(List.of()).stream()
                            .map(authorDTO -> new Author(
                                    authorDTO.getId(),
                                    authorDTO.getName(),
                                    authorDTO.getSurname(), // Добавляем фамилию
                                    authorDTO.getBirthDate() // Добавляем дату рождения
                            ))
                            .collect(Collectors.toList()))
                    .imageId(bd.getImageId())
                    .build();
        }).orElse(null);
    }


}
