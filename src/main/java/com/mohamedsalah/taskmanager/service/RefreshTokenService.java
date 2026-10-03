package com.mohamedsalah.taskmanager.service;

import com.mohamedsalah.taskmanager.entity.RefreshToken;
import com.mohamedsalah.taskmanager.entity.User;

/**
 * Service interface for managing refresh tokens.
 */
public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken validateRefreshToken(String token);
    void deleteByUser(User user);
}
