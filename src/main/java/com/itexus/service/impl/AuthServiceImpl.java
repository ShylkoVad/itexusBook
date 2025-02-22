package com.itexus.service.impl;

import com.itexus.config.JwtProvider;
import com.itexus.domain.User;
import com.itexus.dto.AuthResponse;
import com.itexus.dto.UserCredentialsRequest;
import com.itexus.service.AuthService;
import com.itexus.service.UserService;
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
        User user = userService.findByEmailAndPassword(request.getLogin(), request.getPassword());

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
