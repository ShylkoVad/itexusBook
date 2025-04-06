package com.itexus.genre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients  // Включаем поддержку Feign
@SpringBootApplication
@EnableDiscoveryClient // Включаем поддержку обнаружения сервисов
public class GenreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenreServiceApplication.class, args);
    }

}
