package com.itexus.user.config;

import com.itexus.user.domain.CustomUserDetails;
import com.itexus.user.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class JwtFilter implements WebFilter {

    public static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {

        // Логируем заголовки запроса
        System.out.println("Request headers: " + exchange.getRequest().getHeaders());

        // Получаем токен из заголовка запроса
        String token = getTokenFromRequest(exchange);
        System.out.println("Received token: " + token); // Логируем полученный токен

        // Проверяем, валиден ли токен
        if (token != null && jwtProvider.validateAccessToken(token)) {
            // Получаем логин пользователя из токена
            String userLogin = jwtProvider.getAccessClaims(token).getSubject();

            // Загружаем детали пользователя
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userLogin);

            // Создаем объект аутентификации
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.getAuthorities());

            // Устанавливаем аутентификацию в контекст безопасности
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // Логируем предупреждение о недействительном или отсутствующем токене
            System.out.println("Invalid or missing token");
        }

        // Продолжаем выполнение цепочки фильтров
        return chain.filter(exchange);
    }

    private String getTokenFromRequest(ServerWebExchange exchange) {
        String bearer = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
        log.info("Authorization header: {}", bearer); // Логируем заголовок

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}