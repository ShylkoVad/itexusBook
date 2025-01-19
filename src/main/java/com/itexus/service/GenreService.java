package com.itexus.service;

import com.itexus.dto.BookDTO;
import com.itexus.dto.GenreDTO;

import java.util.List;

public interface GenreService {
    GenreDTO findByIdGenre(Long id);

    List<GenreDTO> findAllGenres();

    GenreDTO saveGenre(GenreDTO genreDTO);

    void deleteGenre(Long id);

    GenreDTO updateGenre(GenreDTO genreDTO);
}
