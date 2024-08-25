package com.itexus.repository.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.itexus.domain.Book;
import com.itexus.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private static final String CSV_FILE_PATH = "src/main/resources/books.csv";
    private static final char COLUMN_SEPARATOR = ';';
    private final File csvFile = new File(CSV_FILE_PATH);
    private final CsvMapper csvMapper = new CsvMapper(); // класс из библиотеки Jackson, предназначенный для работы с CSV данными

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
        CsvSchema schema = CsvSchema.emptySchema()
                .withHeader() // использовать заголовки
                .withColumnSeparator(COLUMN_SEPARATOR); // установить ";" в качестве разделителя

        try (BufferedReader reader = Files.newBufferedReader(csvFile.toPath(), StandardCharsets.UTF_8)) {
            MappingIterator<Book> it = csvMapper.readerFor(Book.class)
                    .with(schema)
                    .readValues(reader);

            return it.readAll();
        }
    }

    // Метод для записи данных в CSV
    private void writeBooksToCsv(List<Book> books) {

        // Проверка на пустой список книг
        if (books == null || books.isEmpty()) {
            System.out.println("Список книг пуст, ничего не записано в файл.");
            return;
        }

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
                .addColumn("id")
                .addColumn("title")
                .addColumn("author")
                .addColumn("description")
                .setUseHeader(true)
                .setColumnSeparator(COLUMN_SEPARATOR)  // разделитель столбцов
                .build();

        File csvFile = new File(CSV_FILE_PATH);

        try {
            // Если файл не существует, создадим новый файл с заголовком
            if (!csvFile.exists()) {
                // Записываем пустой список для создания заголовка
                csvMapper.writer(schema).writeValue(csvFile, new ArrayList<Book>());
            }

            // Записываем данные в файл (в режиме добавления)
            csvMapper.writer(schema).writeValue(csvFile, books);
            System.out.println("Данные успешно записаны в файл books.csv.");
        } catch (IOException e) {
            System.err.println("Ошибка записи в CSV файл: " + e.getMessage());
        }
    }

}
