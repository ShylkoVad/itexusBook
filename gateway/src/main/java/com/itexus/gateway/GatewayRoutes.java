package com.itexus.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutes {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Маршрут для авторов
                .route("author_service", r -> r.path("/authors/**")
                        .uri("lb://author-service")) // Используем lb:// для Load Balancing

                // Маршрут для книг
                .route("book_service", r -> r.path("/books/**")
                        .uri("lb://book-service"))

                // Маршрут для жанров
                .route("genre_service", r -> r.path("/genres/**")
                        .uri("lb://genre-service"))

                // Маршрут для пользователей
                .route("user_service", r -> r.path("/users/**")
                        .uri("lb://user-service"))

                .build();
    }
}
