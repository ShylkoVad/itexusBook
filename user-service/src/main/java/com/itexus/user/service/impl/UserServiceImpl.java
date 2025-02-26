package com.itexus.user.service.impl;

import com.itexus.user.domain.Role;
import com.itexus.user.domain.User;
import com.itexus.user.dto.RoleDTO;
import com.itexus.user.dto.UserDTO;
import com.itexus.user.dto.converters.UserConverters;
import com.itexus.user.repository.RoleRepository;
import com.itexus.user.repository.UserRepository;
import com.itexus.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Data
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverters userConverters;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream().map(userConverters::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByIdUser(Long id) {
        return userConverters.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено", id))));
    }

    @Override
    public UserDTO saveUser (UserDTO userDTO) {
        // Проверяем, существует ли пользователь с таким email
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует.");
        }

        Role role = roleRepository.findByName("USER");
        if (role == null) {
            throw new NoSuchElementException("Роль с именем 'USER' не найдена.");
        }

        User user = userConverters.fromDTO(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Хешируем пароль
        user.setRoles(List.of(role));
        user = userRepository.save(user);

        return userConverters.toDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено.", id)));
        userRepository.delete(user);
    }

    @Override
    public UserDTO updateUserRole(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено.", userDTO.getId())));
        if (!userDTO.getRoles().isEmpty()) {
            List<Role> roles = new ArrayList<>();
            userDTO.getRoles().forEach(r -> roles.add(roleRepository.findByName(r.getName())));
            user.setRoles(roles);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userConverters.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с id %d не найдено.", userDTO.getId())));

        // Обновление основных полей пользователя
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        // Условное шифрование пароля
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // Обновление ролей пользователя
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            List<Role> roles = new ArrayList<>();
            for (RoleDTO roleDTO : userDTO.getRoles()) {
                Role role = roleRepository.findByName(roleDTO.getName());
                if (role != null) {
                    roles.add(role);
                } else {
                    throw new EntityNotFoundException(String.format("Роль с именем %s не найдена.", roleDTO.getName()));
                }
            }
            user.setRoles(roles);
        }
        return userConverters.toDTO(userRepository.save(user));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с почтой %s не найдено.", email)));
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Проверяем, совпадает ли введенный пароль с хешированным паролем в базе данных
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user; // Возвращаем пользователя, если пароли совпадают
            }
        }
        return null; // Возвращаем null, если пользователь не найден или пароль неверный
    }


}
