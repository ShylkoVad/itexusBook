package com.itexus.controller;

import com.itexus.dto.AuthResponse;
import com.itexus.dto.UserCredentialsRequest;
import com.itexus.dto.UserDTO;
import com.itexus.service.AuthService;
import com.itexus.service.UserService;
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

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminOnly() {
        return "Доступ к администратору разрешен!";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> auth(@RequestBody UserCredentialsRequest request) throws AuthException {
        try {
            AuthResponse authResponse = authService.login(request);
            return ResponseEntity.ok(authResponse);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.saveUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
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


}
