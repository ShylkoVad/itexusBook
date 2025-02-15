package com.itexus.service.impl;

import com.itexus.domain.Author;
import com.itexus.dto.AuthorDTO;
import com.itexus.dto.converters.AuthorConverters;
import com.itexus.repository.AuthorRepository;
import com.itexus.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorConverters authorConverters;

    @Override
    public AuthorDTO findByIdAuthor(Long id) {
        return authorConverters.toDTO(authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Автора с id %d не найдено.", id))));
    }

    @Override
    public List<AuthorDTO> findAllAuthors() {
        return authorRepository.findAll().stream().map(authorConverters::toDTO).toList();
    }

    @Override
    public AuthorDTO saveAuthor(AuthorDTO authorDTO) {
        Author author = authorConverters.fromDTO(authorDTO);
        author = authorRepository.save(author);
        return authorConverters.toDTO(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Автора с id %d не найдено.", id)));
        authorRepository.delete(author);
    }

    @Override
    public AuthorDTO updateAuthor(AuthorDTO authorDTO) {
        Author author = authorRepository.findById(authorDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Автора с id %d не найдено.", authorDTO.getId())));
        author.setName(authorDTO.getName());
        author.setSurname(authorDTO.getSurname());
        author.setBirthDate(authorDTO.getBirthDate());
        return authorConverters.toDTO(authorRepository.save(author));
    }

    @Override
    public List<AuthorDTO> findAuthorsByBookId(Long bookId) {
        List<Author> authors = authorRepository.findAuthorsByBookId(bookId);
        return authors.stream()
                .map(authorConverters::toDTO) // Используем конвертер
                .collect(Collectors.toList());
    }
}
