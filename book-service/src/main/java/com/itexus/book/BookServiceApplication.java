package com.itexus.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients  // Включаем поддержку Feign
@SpringBootApplication
@EnableDiscoveryClient // Включаем поддержку обнаружения сервисов
@EntityScan(basePackages = {"com.itexus.book.domain", "com.itexus.author.domain", "com.itexus.genre.domain"})
public class BookServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookServiceApplication.class, args);
    }

}
