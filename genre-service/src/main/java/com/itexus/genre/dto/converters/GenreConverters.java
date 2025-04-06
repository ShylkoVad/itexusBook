package com.itexus.genre.dto.converters;

import com.itexus.genre.domain.Genre;
import com.itexus.genre.dto.GenreDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GenreConverters {

    public GenreDTO toDTO(Genre genre) {
        return Optional.ofNullable(genre).map(g -> GenreDTO.builder()
                        .id(g.getId())
                        .name(g.getName())
                        .build())
                .orElse(null);
    }

    public Genre fromDTO(GenreDTO genreDTO) {
        return Optional.ofNullable(genreDTO).map(gd -> Genre.builder()
                        .name(gd.getName())
                        .build())
                .orElse(null);
    }
}
