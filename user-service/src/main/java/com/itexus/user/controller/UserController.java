package com.itexus.user.controller;

import com.itexus.user.domain.User;
import com.itexus.user.dto.AuthResponse;
import com.itexus.user.dto.UserCredentialsRequest;
import com.itexus.user.dto.UserDTO;
import com.itexus.user.service.AuthService;
import com.itexus.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminOnly() {
        return "Доступ к администратору разрешен!";
    }

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody UserCredentialsRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("Email и пароль не могут быть null");
        }

        log.info("Аутентификация для email: {}", request.getEmail());
        try {
            return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.saveUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> userDTO = userService.findAllUsers();
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findByIdUser(@PathVariable Long id) {
        UserDTO userDTO = userService.findByIdUser(id);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/updateRole")
    public ResponseEntity<UserDTO> updateUserRole(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUserRole(userDTO), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
