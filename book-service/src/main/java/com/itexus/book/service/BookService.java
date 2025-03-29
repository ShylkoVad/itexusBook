package com.itexus.book.service;

import com.itexus.book.dto.BookDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {

    List<BookDTO> findAllBooks();

    BookDTO findByIdBook(Long id);

    BookDTO saveBook(BookDTO bookDTO);

    BookDTO updateBook(BookDTO bookDTO);

    void deleteBook(Long id);

    String uploadImage(Long bookId, MultipartFile file) throws IOException;

    byte[] getImage(Long bookId);

    List<Long> getAuthorIdsByBookId(Long bookId);

}
