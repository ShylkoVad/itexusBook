package com.itexus.repository.impl;

import com.itexus.domain.Book;
import com.itexus.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private static final String CSV_FILE_PATH = "src/main/resources/books.csv";
    private static final String COLUMN_SEPARATOR = ";";
    private final File csvFile = new File(CSV_FILE_PATH);


    @Override
    public List<Book> findAll() {
        try {
            return readBooksFromCsv();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Book findById(Long id) {
        return findAll().stream().filter(book -> book.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void save(Book book) {
        List<Book> books = findAll();
        books.add(book);
        writeBooksToCsv(books);
    }

    @Override
    public void update(Book book) {
        List<Book> books = findAll();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(book.getId())) {
                books.set(i, book);
                break;
            }
        }
        writeBooksToCsv(books);
    }

    @Override
    public void delete(Long id) {
        List<Book> books = findAll();
        books.removeIf(book -> book.getId().equals(id));
        writeBooksToCsv(books);
    }

    // Метод для чтения данных из CSV
    private List<Book> readBooksFromCsv() throws IOException {
        List<Book> books = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            // Пропуск заголовка
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(COLUMN_SEPARATOR);
                if (data.length == 4) {
                    try {
                        long id = Long.parseLong(data[0]);
                        String title = data[1].trim();
                        String author = data[2].trim();
                        String description = data[3].trim();

                        if (!title.isEmpty() && !author.isEmpty()) {
                            books.add(new Book(id, title, author, description));
                        } else {
                            System.err.println("Заголовок и автор не могут быть пустыми для строки: " + line);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Неверный формат идентификатора в строке: " + line);
                    }
                } else {
                    System.err.println("Неправильное количество столбцов в строке: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения csv файла: " + e.getMessage());
            e.printStackTrace();
        }

        return books;
    }

    // Метод для записи данных в CSV
    private void writeBooksToCsv(List<Book> books) {

        // Проверка на пустой список книг
        if (books == null || books.isEmpty()) {
            System.out.println("Список книг пуст, ничего не записано в файл.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, false))) { // Открываем в режиме перезаписи
            writer.write("id;title;author;description");
            writer.newLine();

            // Запись данных в файл
            for (Book book : books) {
                writer.write(book.getId() + COLUMN_SEPARATOR
                        + book.getTitle() + COLUMN_SEPARATOR
                        + book.getAuthor() + COLUMN_SEPARATOR
                        + book.getDescription());
                writer.newLine();
            }

            // Записываем данные в файл (в режиме добавления)
            System.out.println("Данные успешно записаны в файл " + CSV_FILE_PATH + ".");
        } catch (IOException e) {
            System.err.println("Ошибка записи в CSV файл: " + e.getMessage());
        }
    }

}
