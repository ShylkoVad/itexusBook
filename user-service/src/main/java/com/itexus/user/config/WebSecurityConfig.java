package com.itexus.user.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Отключаем CSRF, если это необходимо
                .authorizeExchange(exchange -> exchange
//                                .anyExchange().permitAll() // Разрешаем доступ ко всем запросам
//                        .pathMatchers("/actuator/health").permitAll() // Разрешаем доступ к проверке состояния
                        .pathMatchers("/users/register", "/users/login").permitAll() // Разрешаем доступ к регистрации и логину
                        .anyExchange().authenticated() // Все остальные запросы требуют аутентификации
                )
                .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHORIZATION); // Добавляем JWT фильтр перед авторизацией
        return http.build();
    }
}