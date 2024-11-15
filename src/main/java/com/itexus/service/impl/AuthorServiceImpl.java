package com.itexus.service.impl;

import com.itexus.domain.Author;
import com.itexus.repository.AuthorRepository;
import com.itexus.service.AuthorService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public void save(Author author) {
        authorRepository.save(author);
    }

    @Override
    public void delete(Long id) {
        authorRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> findAuthorsByBookId(Long bookId) {
        return authorRepository.findAuthorsByBookId(bookId);
    }
}
