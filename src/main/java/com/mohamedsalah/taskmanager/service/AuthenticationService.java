package com.mohamedsalah.taskmanager.service;

import com.mohamedsalah.taskmanager.dto.request.LoginRequest;
import com.mohamedsalah.taskmanager.dto.request.RefreshTokenRequest;
import com.mohamedsalah.taskmanager.dto.request.RegisterRequest;
import com.mohamedsalah.taskmanager.dto.response.AuthenticationResponse;
import com.mohamedsalah.taskmanager.dto.response.UserResponse;

/**
 * Service interface for handling authentication operations.
 */
public interface AuthenticationService {
    UserResponse register(RegisterRequest request);
    AuthenticationResponse login(LoginRequest request);
    AuthenticationResponse refreshToken(RefreshTokenRequest request);
    void logout(String email);
}
