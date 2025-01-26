package com.itexus.controller;

import com.itexus.dto.BookDTO;
import com.itexus.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> findAllBooks() {
        List<BookDTO> booksDTO = bookService.findAllBooks();
        return ResponseEntity.ok(booksDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findByIdBook(@PathVariable Long id) {
        BookDTO bookDTO = bookService.findByIdBook(id);
        return ResponseEntity.ok(bookDTO);
    }

    @PostMapping
    public ResponseEntity<BookDTO> saveBook(@RequestBody BookDTO bookDTO) {
        return new ResponseEntity<>(bookService.saveBook(bookDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO bookDTO) {
        return new ResponseEntity<>(bookService.updateBook(bookDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
