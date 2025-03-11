package com.itexus.user.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Configuration
@ComponentScan(basePackages = "com")
@AllArgsConstructor
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable) // Отключаем CSRF, если это необходимо
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/users/register", "/users/login").permitAll() // Разрешаем доступ к регистрации и логину
//                        .requestMatchers("/admin/**").hasRole("ADMIN") // Только для администраторов
                                .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Добавляем JWT фильтр
        return http.build();
    }
}
