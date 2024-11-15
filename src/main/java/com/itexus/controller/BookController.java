package com.itexus.controller;

import com.itexus.domain.Author;
import com.itexus.domain.Book;
import com.itexus.service.AuthorService;
import com.itexus.service.BookService;
import com.itexus.util.ApplicationContext;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
@Controller
public class BookController {
    private final BookService bookService;  // Сервис для управления книгами
    private final MessageSource messageSource; // Источник сообщений для локализации
    private Locale locale; // Локаль для сообщений
    private final AuthorService authorService; // Сервис для управления авторами

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

        // Устанавливаем локаль в глобальный контекст
        ApplicationContext.getInstance().setLocale(locale);

        System.out.println(messageSource.getMessage("welcome.message", null, locale));

        while (true) {
            displayMenu();  // Отображение доступных действий

            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера ввода

            switch (choice) {
                case 1:
                    findAllBooks(); // Получение всех книг
                    break;
                case 2:
                    saveBook(scanner); // Сохранение новой книги
                    break;
                case 3:
                    updateBook(scanner); // Обновление книги
                    break;
                case 4:
                    deleteBook(scanner); // Удаление книги
                    break;
                case 5:
                    findByIdBook(scanner); // Поиск книги по ID
                    break;
                case 6:
                    return; // Выход
                default:
                    System.out.println(messageSource.getMessage("message.select", null, locale));
            }
        }
    }

    // Отображение меню
    private void displayMenu() {
        System.out.println(messageSource.getMessage("option.select", null, locale));
        System.out.println(messageSource.getMessage("findAllBooks", null, locale));
        System.out.println(messageSource.getMessage("saveBook", null, locale));
        System.out.println(messageSource.getMessage("updateBook", null, locale));
        System.out.println(messageSource.getMessage("deleteBook", null, locale));
        System.out.println(messageSource.getMessage("findByIdBook", null, locale));
        System.out.println(messageSource.getMessage("exit", null, locale));
    }

    // Получение всех книг
    private void findAllBooks() {
        List<Book> books = bookService.findAll();
        if (books.isEmpty()) {
            System.out.println(messageSource.getMessage("message.listEmpty", null, locale));
        } else {
            System.out.printf("%-5s | %-30s | %-50s | %-15s%n", "ID", "Title", "Description", "Published Date", "Author");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            for (Book book : books) {

                // Получаем авторов для текущей книги
                List<Author> authors = authorService.findAuthorsByBookId(book.getId());

                // Формируем строку с именами авторов
                StringBuilder authorsNames = new StringBuilder();
                authors.forEach(author -> authorsNames.append(author.getName()).append(" ").append(author.getSurname()));

                // Выводим данные о книге и авторах
                System.out.printf("%-5d | %-30s | %-50s | %-15s | %-30s%n",
                        book.getId(), book.getTitle(), book.getDescription(), book.getPublishedDate(), authorsNames);
            }
        }
    }

    private void saveBook(Scanner scanner) {
        System.out.println(messageSource.getMessage("input.title", null, locale));
        String title = scanner.nextLine();

        System.out.println(messageSource.getMessage("input.description", null, locale));
        String description = scanner.nextLine();

        System.out.println(messageSource.getMessage("input.published.date", null, locale));
        String publishedDate = scanner.nextLine();

        System.out.println(messageSource.getMessage("input.author.ids", null, locale)); // Добавить ввод авторов
        String authorIdsInput = scanner.nextLine();
        List<Long> authorIds = parseIds(authorIdsInput);

        System.out.println(messageSource.getMessage("input.genre.ids", null, locale)); // Добавить ввод жанров
        String genreIdsInput = scanner.nextLine();
        List<Long> genreIds = parseIds(genreIdsInput);

        // Создание объекта книги с учетом всех полей
        Book book = new Book(null, title, description, LocalDate.parse(publishedDate), authorIds, genreIds);

        // Сохранение книги через сервис и получение сгенерированного идентификатора
        Long savedBookId = bookService.save(book);

        // Установка ID в объекте book, если нужно
        book.setId(savedBookId);

        // Добавление авторов и жанров к книге
        for (Long authorId : authorIds) {
            bookService.addAuthorToBook(book.getId(), authorId);
        }

        for (Long genreId : genreIds) {
            bookService.addGenreToBook(book.getId(), genreId);
        }

        System.out.println(messageSource.getMessage("message.saveBook", null, locale));
    }

    // Метод для преобразования строки введенных идентификаторов в список Long
    private List<Long> parseIds(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim) // Удаляем пробелы
                .map(Long::valueOf) // Преобразуем в Long
                .collect(Collectors.toList()); // Собираем в список
    }

    // Обновление книги
    private void updateBook(Scanner scanner) {
        System.out.println(messageSource.getMessage("input.book.updateId", null, locale));
        Long bookId = scanner.nextLong();
        scanner.nextLine(); // Очистка буфера ввода

        System.out.println(messageSource.getMessage("input.updateTitle", null, locale));
        String title = scanner.nextLine();

        System.out.println(messageSource.getMessage("input.updateDescription", null, locale));
        String description = scanner.nextLine();

        System.out.println(messageSource.getMessage("input.published.updateDate", null, locale));
        String publishedDate = scanner.nextLine();

        System.out.println(messageSource.getMessage("input.author.updateIds", null, locale)); // Добавить ввод авторов
        String authorIdsInput = scanner.nextLine();
        List<Long> authorIds = parseIds(authorIdsInput);

        System.out.println(messageSource.getMessage("input.genre.updateIds", null, locale)); // Добавить ввод жанров
        String genreIdsInput = scanner.nextLine();
        List<Long> genreIds = parseIds(genreIdsInput);

        // Обновление книги
        Book book = new Book(bookId, title, description, LocalDate.parse(publishedDate), authorIds, genreIds);
        bookService.update(book); // Обновление через сервис
        System.out.println(messageSource.getMessage("book.updated", null, locale));
    }

    // Удаление книги
    private void deleteBook(Scanner scanner) {
        System.out.println(messageSource.getMessage("input.book.deleteId", null, locale));
        Long bookId = scanner.nextLong();
        bookService.delete(bookId); // Удаление книги через сервис
        System.out.println(messageSource.getMessage("book.deleted", null, locale));
    }

    // Поиск книги по ID
    private void findByIdBook(Scanner scanner) {
        System.out.println(messageSource.getMessage("input.book.id", null, locale));
        Long bookId = scanner.nextLong();
        Book book = bookService.findById(bookId); // Поиск книги через сервис
        if (book != null) {
            System.out.printf("%-5s | %-30s | %-50s | %-15s%n", "ID", "Title", "Description", "Published Date", "Author");
            System.out.println("--------------------------------------------------------------------------------------------------");
            // Получаем авторов для книги
            List<Author> authors = authorService.findAuthorsByBookId(book.getId());

            // Формируем строку с именами авторов
            StringBuilder authorsNames = new StringBuilder();
            authors.forEach(author -> authorsNames.append(author.getName()).append(" ").append(author.getSurname()));

            // Выводим информацию о книге и авторах
            System.out.printf("%-5d | %-30s | %-50s | %-15s | %-30s%n",
                    book.getId(), book.getTitle(), book.getDescription(), book.getPublishedDate(), authorsNames.toString());
        } else {
            System.out.println(messageSource.getMessage("book.not.found", null, locale));
        }
    }
}
