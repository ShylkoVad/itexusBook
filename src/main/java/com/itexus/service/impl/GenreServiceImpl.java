package com.itexus.service.impl;

import com.itexus.domain.Genre;
import com.itexus.dto.GenreDTO;
import com.itexus.dto.converters.GenreConverters;
import com.itexus.repository.GenreRepository;
import com.itexus.service.GenreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreConverters genreConverters;

    @Override
    public List<GenreDTO> findAllGenres() {
        return genreRepository.findAll().stream().map(genreConverters::toDTO).toList();
    }

    @Override
    public GenreDTO findByIdGenre(Long id) {
        return genreConverters.toDTO(genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Жанра с id %d не найдено.", id))));
    }

    @Override
    public GenreDTO saveGenre(GenreDTO genreDTO) {
        Genre genre = genreConverters.fromDTO(genreDTO);
        genre = genreRepository.save(genre);
        return genreConverters.toDTO(genre);
    }

    @Override
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Жанра с id %d не найдено.", id)));
        genreRepository.delete(genre);
    }

    @Override
    public GenreDTO updateGenre(GenreDTO genreDTO) {
        Genre genre = genreRepository.findById(genreDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Жанра с id %d не найдено.", genreDTO.getId())));
        genre.setName(genreDTO.getName());
        return genreConverters.toDTO(genreRepository.save(genre));
    }
}
