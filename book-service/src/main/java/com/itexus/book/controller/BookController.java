package com.itexus.book.controller;

import com.itexus.book.dto.BookDTO;
import com.itexus.book.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> findAllBooks() {
        List<BookDTO> booksDTO = bookService.findAllBooks();
        return ResponseEntity.ok(booksDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findByIdBook(@PathVariable Long id) {
        log.info("Получен запрос на загрузку книги с ID: {}", id); // Логируем переданный ID
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

    // Метод для загрузки изображения
//    @PostMapping("/{id}/upload-image")
//    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
//        log.info("Метод uploadImage вызван для книги с ID: {}", id); // Временный лог
//        log.info("Получен запрос на загрузку изображения для книги с ID: {}", id); // Логируем переданный ID
//        try {
//            String imageId = bookService.uploadImage(id, file); // Метод в сервисе для обработки загрузки
//            return ResponseEntity.ok(imageId);
//        } catch (IOException e) {
//            log.error("Ошибка загрузки изображения для книги с ID: {}. Ошибка: {}", id, e.getMessage()); // Логируем ошибку
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка загрузки изображения");
//        }
//    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Логируем ID книги
        log.info("Метод uploadImage вызван для книги с ID: {}", id);

        // Возвращаем просто ID в ответе
        return ResponseEntity.ok("ID книги: " + id);
    }

    // Метод для выгрузки изображения
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        byte[] imageData = bookService.getImage(id); // Метод в сервисе для получения изображения
        if (imageData != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Указать тип изображения
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
