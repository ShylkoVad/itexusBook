package com.itexus.author.service;

import com.itexus.author.dto.AuthorDTO;

import java.util.List;

public interface AuthorService {
    AuthorDTO findByIdAuthor(Long id);

    List<AuthorDTO> findAllAuthors();

    AuthorDTO saveAuthor(AuthorDTO authorDTO);

    void deleteAuthor(Long id);

    AuthorDTO updateAuthor(AuthorDTO authorDTO);

}
