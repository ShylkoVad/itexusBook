package com.itexus.service;

import com.itexus.domain.Book;
import com.itexus.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final Map<Long, Book> cache = new HashMap<>();

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
        cache.remove(book.getId()); // Очистить кэш
    }

    public void updateBook(Book book) {
        bookRepository.update(book);
        cache.put(book.getId(), book); // Очистить кэш, для избежания старых данных
    }

    public void deleteBook(Long id) {
        bookRepository.delete(id);
        cache.remove(id);  // Удаляем из кэша при удалении
    }

    public Book findByIdBook(Long id) {
        // Проверяем кэш перед обращением в репозиторий
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        // Если книги нет в кэше, ищем в репозитории
        Book book = bookRepository.findById(id);
        if (book != null) {
            cache.put(id, book); // Кэшируем результат
        }

        return book;
    
    }
}
