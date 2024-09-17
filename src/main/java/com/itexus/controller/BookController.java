package com.itexus.controller;

import com.itexus.domain.Book;
import com.itexus.service.BookService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Controller
public class BookController {
    private final BookService bookService;
    private final MessageSource messageSource;
    Locale locale;

    public BookController(BookService bookService, MessageSource messageSource) {
        this.bookService = bookService;
        this.messageSource = messageSource;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        //Выбор языка
        System.out.println("Выберите язык / Chose language (ru / en):");
        String langChoice = scanner.nextLine();
        if ("ru".equalsIgnoreCase(langChoice)) {
            locale = Locale.forLanguageTag("ru");
        } else {
            locale = Locale.forLanguageTag("en");
        }
        System.out.println(messageSource.getMessage("welcome.message", null, locale));

        while (true) {
            System.out.println(messageSource.getMessage("option.select", null, locale));
            System.out.println(messageSource.getMessage("findAllBooks", null, locale));
            System.out.println(messageSource.getMessage("saveBook", null, locale));
            System.out.println(messageSource.getMessage("updateBook", null, locale));
            System.out.println(messageSource.getMessage("deleteBook", null, locale));
            System.out.println(messageSource.getMessage("findByIdBook", null, locale));
            System.out.println(messageSource.getMessage("exit", null, locale));

            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера ввода

            switch (choice) {
                case 1:
                    findAllBooks();
                    break;
                case 2:
                    saveBook(scanner);
                    break;
                case 3:
                    updateBook(scanner);
                    break;
                case 4:
                    deleteBook(scanner);
                    break;
                case 5:
                    findByIdBook(scanner);
                    break;
                case 6:
                    return;
                default:
                    System.out.println(messageSource.getMessage("message.select", null, locale));
            }
        }
    }

    private void findAllBooks() {
        List<Book> bookList = bookService.findAllBooks();
        // Проверка на наличие книг
        if (bookList == null || bookList.isEmpty()) {
            System.out.println(messageSource.getMessage("message.listEmpty", null, locale));
            return;
        }
        // Вывод информации о каждой книге
        for (Book book : bookList) {
            String output = String.format("%d: %s, %s, %s",
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getDescription());
            System.out.println(output);
        }
    }

    private void saveBook(Scanner scanner) {
        System.out.println(messageSource.getMessage("message.title", null, locale));
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println(messageSource.getMessage("message.titleEmpty", null, locale));
            return;
        }

        System.out.println(messageSource.getMessage("message.author", null, locale));
        String author = scanner.nextLine().trim();

        if (author.isEmpty()) {
            System.out.println(messageSource.getMessage("message.authorEmpty", null, locale));
            return;
        }

        System.out.println(messageSource.getMessage("message.description", null, locale));

        String description = scanner.nextLine().trim();  // Можно обрабатывать описания по правилам

        Long id = generateUniqueId(); // Переход на метод генерации уникального ID
        Book book = new Book(id, title, author, description);

        try {
            bookService.saveBook(book);
            System.out.println(messageSource.getMessage("message.saveBook", null, locale));

        } catch (Exception e) {
            System.out.println(messageSource.getMessage("message.saveBookError", null, locale) + e.getMessage());

        }
    }

    private Long generateUniqueId() {
        // Логика для генерации уникального ID
        return (long) (bookService.findAllBooks().size() + 1);
    }

    private void updateBook(Scanner scanner) {
        System.out.println(messageSource.getMessage("message.updateId", null, locale));

        Long id = scanner.nextLong();
        scanner.nextLine(); // очистка буфера
        Book existingBook = bookService.findByIdBook(id);
        if (existingBook == null) {
            System.out.println(messageSource.getMessage("message.bookNotFound", null, locale));

            return;
        }
        System.out.println(messageSource.getMessage("message.updateTitle", null, locale));

        String title = scanner.nextLine();
        System.out.println(messageSource.getMessage("message.updateAuthor", null, locale));

        String author = scanner.nextLine();
        System.out.println(messageSource.getMessage("message.updateDescription", null, locale));

        String description = scanner.nextLine();

        if (!title.isEmpty()) existingBook.setTitle(title);
        if (!author.isEmpty()) existingBook.setAuthor(author);
        if (!description.isEmpty()) existingBook.setDescription(description);

        bookService.updateBook(existingBook);
        System.out.println(messageSource.getMessage("message.updateBook", null, locale));

    }

    private void deleteBook(Scanner scanner) {
        System.out.println(messageSource.getMessage("message.deleteId", null, locale));

        Long id = scanner.nextLong();
        bookService.deleteBook(id);
    }

    private void findByIdBook(Scanner scanner) {
        System.out.println(messageSource.getMessage("message.findId", null, locale));

        Long id = scanner.nextLong();
        Book existingBook = bookService.findByIdBook(id);
        if (existingBook == null) {
            System.out.println(messageSource.getMessage("message.bookNotFound", null, locale));

            return;
        }
        String output = String.format("%d: %s, %s, %s",
                existingBook.getId(),
                existingBook.getTitle(),
                existingBook.getAuthor(),
                existingBook.getDescription());
        System.out.println(output);
    }
}
