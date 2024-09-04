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

    public BookController(BookService bookService, MessageSource messageSource) {
        this.bookService = bookService;
        this.messageSource = messageSource;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        //Выбор языка
        System.out.println("Выберите язык / Chose language (ru / en):");
        String langChoice = scanner.nextLine();
        Locale locale;
        if ("ru".equalsIgnoreCase(langChoice)) {
            locale = new Locale("ru");
        } else {
            locale = new Locale("en");
        }
        System.out.println(messageSource.getMessage("welcome.message", null, locale));

        while (true) {
            System.out.println(messageSource.getMessage("selecting.option", null, locale));
            System.out.println("1. Вывести весь список книг.");
            System.out.println("2. Создать новую книгу.");
            System.out.println("3. Отредактировать книгу.");
            System.out.println("4. Удалить книгу.");
            System.out.println("5. Получить информацию о книге");
            System.out.println("6. Выход.");

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
                    System.out.println("Выбор сделан неверно. Повторите пожалуйста попытку.");
            }
        }
    }

    private void findAllBooks() {
        List<Book> bookList = bookService.findAllBooks();
        // Проверка на наличие книг
        if (bookList == null || bookList.isEmpty()) {
            System.out.println("Список книг пуст.");
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
        System.out.println("Введите название книги: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Название книги не может быть пустым.");
            return;
        }

        System.out.println("Введите имя автора: ");
        String author = scanner.nextLine().trim();

        if (author.isEmpty()) {
            System.out.println("Строка автора не может быть пустой.");
            return;
        }

        System.out.println("Введите краткое описание: ");
        String description = scanner.nextLine().trim();  // Можно обрабатывать описания по правилам

        Long id = generateUniqueId(); // Переход на метод генерации уникального ID
        Book book = new Book(id, title, author, description);

        try {
            bookService.saveBook(book);
            System.out.println("Книга успешно сохранена.");
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении книги: " + e.getMessage());
        }
    }

    private Long generateUniqueId() {
        // Логика для генерации уникального ID
        return (long) (bookService.findAllBooks().size() + 1);
    }

    private void updateBook(Scanner scanner) {
        System.out.println("Введите id книги для редактирования:");
        Long id = scanner.nextLong();
        scanner.nextLine(); // очистка буфера
        Book existingBook = bookService.findByIdBook(id);
        if (existingBook == null) {
            System.out.println("Данная книга не найдена.");
            return;
        }
        System.out.println("Введите новое название книги или оставьте пустым (для сохранения текущего):");
        String title = scanner.nextLine();
        System.out.println("Введите новое имя автора или оставьте пустым (для сохранения текущего):");
        String author = scanner.nextLine();
        System.out.println("Введите новое описание книги или оставьте пустым (для сохранения текущего):");
        String description = scanner.nextLine();

        if (!title.isEmpty()) existingBook.setTitle(title);
        if (!author.isEmpty()) existingBook.setAuthor(author);
        if (!description.isEmpty()) existingBook.setDescription(description);

        bookService.updateBook(existingBook);
        System.out.println("Книга успешно обновлена.");
    }

    private void deleteBook(Scanner scanner) {
        System.out.println("Введите id книги для удаления:");
        Long id = scanner.nextLong();
        bookService.deleteBook(id);
    }

    private void findByIdBook(Scanner scanner) {
        System.out.println("Введите id книги для получения информации:");
        Long id = scanner.nextLong();
        Book existingBook = bookService.findByIdBook(id);
        if (existingBook == null) {
            System.out.println("Данная книга не найдена.");
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
