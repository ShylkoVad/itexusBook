package com.itexus.user.service;

import com.itexus.user.dto.AuthResponse;
import com.itexus.user.dto.UserCredentialsRequest;
import jakarta.security.auth.message.AuthException;

public interface AuthService {

    AuthResponse login(UserCredentialsRequest request) throws AuthException;

}
