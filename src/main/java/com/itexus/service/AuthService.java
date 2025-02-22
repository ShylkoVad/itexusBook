package com.itexus.service;

import com.itexus.dto.AuthResponse;
import com.itexus.dto.UserCredentialsRequest;
import jakarta.security.auth.message.AuthException;

public interface AuthService {

    AuthResponse login(UserCredentialsRequest request) throws AuthException;

}
