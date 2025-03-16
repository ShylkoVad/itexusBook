package com.itexus.user.service.impl;

import com.itexus.user.config.JwtProvider;
import com.itexus.user.domain.User;
import com.itexus.user.dto.AuthResponse;
import com.itexus.user.dto.UserCredentialsRequest;
import com.itexus.user.service.AuthService;
import com.itexus.user.service.UserService;
import jakarta.security.auth.message.AuthException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Override
    public AuthResponse login(UserCredentialsRequest request) throws AuthException {
        // Поиск пользователя по логину и паролю
        User user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());

        // Проверка, найден ли пользователь
        if (user != null) {
            // Генерация access-токена
            String accessToken = jwtProvider.generateAccessToken(user.getEmail());
            return new AuthResponse(accessToken);
        }

        // Если пользователь не найден, выбрасываем исключение
        throw new AuthException("Неверный логин или пароль");
    }
}
