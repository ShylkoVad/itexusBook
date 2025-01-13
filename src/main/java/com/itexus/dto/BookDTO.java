package com.itexus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate publishedDate;
    private GenreDTO genre; // Если хотим передавать информацию о жанре
    private List<AuthorDTO> authors; // Список авторов
}
