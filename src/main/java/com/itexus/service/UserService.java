package com.itexus.service;

import com.itexus.domain.User;
import com.itexus.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> findAllUsers();

    UserDTO findByIdUser(Long id);

    UserDTO saveUser (UserDTO userDTO);

    void deleteUser(Long id);

    UserDTO updateUserRole(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO);

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);
}
