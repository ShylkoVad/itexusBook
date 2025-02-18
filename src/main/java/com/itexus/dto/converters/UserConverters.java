package com.itexus.dto.converters;

import com.itexus.domain.User;
import com.itexus.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserConverters {

    private final RoleConvertors roleConvertors;

    public UserDTO toDTO(User user) {
        return Optional.ofNullable(user).map(u -> UserDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .password(u.getPassword())
                .email(u.getEmail())
                .roles(u.getRoles().stream()
                        .map(roleConvertors::toDTO) // Преобразование ролей с помощью roleConvertors
                        .collect(Collectors.toList()))
                        .build())
                .orElse(null);
    }

    public User fromDTO(UserDTO userDTO) {
        return Optional.ofNullable(userDTO).map(ud -> User.builder()
                        .username(ud.getUsername())
                        .password(ud.getPassword())
                        .email(ud.getEmail())
                        .roles(ud.getRoles().stream()
                                .map(roleConvertors::fromDTO) // Преобразование ролей с помощью roleConvertors
                                .collect(Collectors.toList())) // Сборка в список
                        .build())
                .orElse(null);
    }
}
