package com.itexus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/Books_library");
        dataSource.setUsername("postgres");
        dataSource.setPassword("1234");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate; // Инициализируем JdbcTemplate через инъекцию

    @PostConstruct
    public void initDatabase() {
        executeSqlScript(jdbcTemplate, "init.sql");
    }

    private void executeSqlScript(JdbcTemplate jdbcTemplate, String scriptPath) {
        try (var resourceStream = getClass().getClassLoader().getResourceAsStream(scriptPath)) {
            if (resourceStream == null) {
                logger.error("SQL script not found: {}", scriptPath);
                return; // Или выбросьте исключение, если нужно
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8))) {
                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    // Игнорировать пустые строки
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    sql.append(line).append("\n");

                    // Если строчка заканчивается на ';', то выполните команду
                    if (line.trim().endsWith(";")) {
                        jdbcTemplate.execute(sql.toString());
                        sql.setLength(0); // Сбросить сборщик строк
                    }
                }
                // Выполнение оставшейся команды, если таковая имеется
                if (sql.length() > 0) {
                    jdbcTemplate.execute(sql.toString());
                }

                logger.info("Successfully executed SQL script: {}", scriptPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute SQL script: " + scriptPath, e);
        } catch (Exception e) {
            logger.error("Error executing SQL script: {}", scriptPath, e);
            throw new RuntimeException("Unexpected error while executing SQL script: " + scriptPath, e);
        }
    }
}
