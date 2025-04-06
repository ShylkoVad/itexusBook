package com.itexus.book.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long bookId) {
        super("Книга с ID " + bookId + " не найдена.");
    }
}
