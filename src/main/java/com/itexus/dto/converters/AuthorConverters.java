package com.itexus.dto.converters;

import com.itexus.domain.Author;
import com.itexus.dto.AuthorDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorConverters {

    public AuthorDTO toDTO(Author author) {
        return Optional.ofNullable(author).map(a -> AuthorDTO.builder()
                        .id(a.getId())
                        .name(a.getName())
                        .surname(a.getSurname())
                        .birthDate(a.getBirthDate())
                        .build())
                .orElse(null);
    }

    public Author fromDTO(AuthorDTO authorDTO) {
        return Optional.ofNullable(authorDTO).map(ad -> Author.builder()
                        .name(ad.getName())
                        .surname(ad.getSurname())
                        .birthDate(ad.getBirthDate())
                        .build())
                .orElse(null);
    }
}
