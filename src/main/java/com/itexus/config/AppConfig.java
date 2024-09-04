package com.itexus.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@ComponentScan(basePackages = "com.itexus")
public class AppConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        // Базовое имя файлов свойств (без расширения)
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8"); // Кодировка для чтения файлов
        return messageSource;
    }
}
