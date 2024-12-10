package com.itexus.controller;

import com.itexus.domain.Author;
import com.itexus.domain.Book;
import com.itexus.domain.Genre;
import com.itexus.service.AuthorService;
import com.itexus.service.BookService;
import com.itexus.service.GenreService;
import com.itexus.util.ApplicationContext;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
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
    private final GenreService genreService; // Сервис для управления жанрами

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
        List<String> menuOptions = Arrays.asList(
                "option.select",
                "findAllBooks",
                "saveBook",
                "updateBook",
                "deleteBook",
                "findByIdBook",
                "exit"
        );
        for (String option : menuOptions) {
            System.out.println(messageSource.getMessage(option, null, locale));
        }
    }

    // Получение всех книг
    private void findAllBooks() {
        List<Book> books = bookService.findAll();
        if (books.isEmpty()) {
            System.out.println(messageSource.getMessage("message.listEmpty", null, locale));
        } else {
            System.out.printf("%-5s | %-30s | %-50s | %-15s | %-30s | %-30s%n", "ID", "Title", "Description", "Published Date", "Authors", "Genre");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");
            for (Book book : books) {

                List<Author> authors = authorService.findAuthorsByBookId(book.getId()); // Получаем авторов для текущей книги

                // Формируем строку с именами авторов
                String authorsNames = authors.stream()
                        .map(author -> author.getName() + " " + author.getSurname())
                        .collect(Collectors.joining(", "));

                // Получение жанра текущей книги
                Genre genre = book.getGenre();
                String genreName = (genre != null) ? genre.getName() : "No genre"; // Если жанр отсутствует

                // Выводим данные о книге, авторах и жанре
                System.out.printf("%-5d | %-30s | %-50s | %-15s | %-30s | %-30s%n",
                        book.getId(), book.getTitle(), book.getDescription(), book.getPublishedDate(), authorsNames, genreName);
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

        System.out.println(messageSource.getMessage("input.genre.ids", null, locale));
        String genreIdInput = scanner.nextLine();
        Long genreId = Long.valueOf(genreIdInput); // Преобразование ввода в Long

        Set<Author> authors = getAuthorsByIds(new HashSet<>(authorIds)); // Получение объектов авторов

        Genre genre = genreService.findById(genreId); // Поиск жанра по ID

        // Создание объекта книги с учетом всех полей
        Book book = new Book(title, description, LocalDate.parse(publishedDate), authors, genre);

        // Сохранение книги через сервис и получение сгенерированного идентификатора
        Long savedBookId = bookService.save(book);

        System.out.println(messageSource.getMessage("message.saveBook", new Object[]{savedBookId}, locale));
    }

    // Метод для преобразования строки введенных идентификаторов в список Long
    private List<Long> parseIds(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim) // Удаляем пробелы
                .map(Long::valueOf) // Преобразуем в Long
                .collect(Collectors.toList()); // Собираем в список
    }

    private void updateBook(Scanner scanner) {
        System.out.println(messageSource.getMessage("input.book.updateId", null, locale));
        Long bookId = scanner.nextLong();
        scanner.nextLine(); // Очистка буфера ввода

        Book book = bookService.findById(bookId); // Находим книгу по ID

        if (book == null) {
            System.out.println(messageSource.getMessage("book.not.found", null, locale));
            return; // Выход, если книга не найдена
        }

        System.out.println(messageSource.getMessage("input.updateTitle", null, locale));
        String title = scanner.nextLine();

        System.out.println(messageSource.getMessage("input.updateDescription", null, locale));
        String description = scanner.nextLine();

        System.out.println(messageSource.getMessage("input.published.updateDate", null, locale));
        String publishedDate = scanner.nextLine();

        System.out.println(messageSource.getMessage("input.author.updateIds", null, locale));
        String authorIdsInput = scanner.nextLine();
        List<Long> authorIds = parseIds(authorIdsInput);
        Set<Author> authors = getAuthorsByIds(new HashSet<>(authorIds)); // Получение объектов авторов

        System.out.println(messageSource.getMessage("input.genre.updateIds", null, locale));
        String genreIdInput = scanner.nextLine(); // Поменять на одиночный ввод
        Long genreId = Long.valueOf(genreIdInput); // Преобразование ввода в Long

        Genre genre = genreService.findById(genreId); // Поиск жанра по ID

        // Обновление полей книги
        book.setTitle(title);
        book.setDescription(description);
        book.setPublishedDate(LocalDate.parse(publishedDate));
        book.setAuthors(authors);
        book.setGenre(genre); // Устанавливаем один жанр

        // Обновление книги через сервис
        bookService.update(book);
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
            System.out.printf("%-5s | %-30s | %-50s | %-15s | %-30s | %-20s%n",
                    "ID", "Title", "Description", "Published Date", "Authors", "Genre");
            System.out.println("--------------------------------------------------------------------------------------------------");
            // Получаем авторов для книги
            List<Author> authors = authorService.findAuthorsByBookId(book.getId());

            // Формируем строку с именами авторов
            StringBuilder authorsNames = new StringBuilder();
            for (int i = 0; i < authors.size(); i++) {
                Author author = authors.get(i);
                authorsNames.append(author.getName()).append(" ").append(author.getSurname());
                if (i < authors.size() - 1) {
                    authorsNames.append(", "); // Добавляем запятую между авторами
                }
            }


            Genre genre = book.getGenre(); // Получаем жанр книги

            // Выводим информацию о книге, авторах и жанре
            System.out.printf("%-5d | %-30s | %-50s | %-15s | %-30s | %-20s%n",
                    book.getId(),
                    book.getTitle(),
                    book.getDescription(),
                    book.getPublishedDate(),
                    authorsNames,
                    genre != null ? genre.getName() : "N/A");
        } else {
            System.out.println(messageSource.getMessage("book.not.found", null, locale));
        }
    }

    private Set<Author> getAuthorsByIds(Set<Long> authorIds) {
        Set<Author> authors = new HashSet<>();
        for (Long authorId : authorIds) {
            Author author = authorService.findById(authorId); // метод поиска автора по ID
            if (author != null) {
                authors.add(author);
            }
        }
        return authors;
    }

}
