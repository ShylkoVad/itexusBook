package com.itexus.config;

import com.itexus.repository.AuthorRepository;
import com.itexus.repository.BookRepository;
import com.itexus.repository.GenreRepository;
import com.itexus.service.AuthorService;
import com.itexus.service.BookService;
import com.itexus.service.GenreService;
import com.itexus.service.impl.AuthorServiceImpl;
import com.itexus.service.impl.BookServiceImpl;
import com.itexus.service.impl.GenreServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan(basePackages = "com.itexus")
@EnableAspectJAutoProxy
@Import(DatabaseConfig.class) // Импортирую класс DatabaseConfig
public class AppConfig {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public BookRepository bookRepository(JdbcTemplate jdbcTemplate) {
        return new BookRepository(jdbcTemplate);
    }

    @Bean
    public BookService bookService(BookRepository bookRepository) {
        return new BookServiceImpl(bookRepository);
    }

    @Bean
    public AuthorRepository authorRepository(JdbcTemplate jdbcTemplate) {
        return new AuthorRepository(jdbcTemplate);
    }

    @Bean
    public AuthorService authorService(AuthorRepository authorRepository) {
        return new AuthorServiceImpl(authorRepository);
    }

    @Bean
    public GenreRepository genreRepository(JdbcTemplate jdbcTemplate) {
        return new GenreRepository(jdbcTemplate);
    }

    @Bean
    public GenreService genreService(GenreRepository genreRepository) {
        return new GenreServiceImpl(genreRepository);
    }
}
