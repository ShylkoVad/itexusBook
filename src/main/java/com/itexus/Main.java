package com.itexus;

import com.itexus.controller.BookController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(com.itexus.config.AppConfig.class);
        BookController bookController = context.getBean(BookController.class);
        bookController.run();
    }
}