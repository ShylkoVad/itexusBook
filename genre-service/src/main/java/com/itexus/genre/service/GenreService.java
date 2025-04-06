package com.itexus.genre.service;

import com.itexus.genre.dto.GenreDTO;

import java.util.List;

public interface GenreService {

    GenreDTO findByIdGenre(Long id);

    List<GenreDTO> findAllGenres();

    GenreDTO saveGenre(GenreDTO genreDTO);

    void deleteGenre(Long id);

    GenreDTO updateGenre(GenreDTO genreDTO);
}
