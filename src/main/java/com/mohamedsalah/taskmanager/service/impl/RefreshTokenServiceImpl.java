package com.mohamedsalah.taskmanager.service.impl;

import com.mohamedsalah.taskmanager.exception.InvalidTokenException;
import com.mohamedsalah.taskmanager.entity.RefreshToken;
import com.mohamedsalah.taskmanager.entity.User;
import com.mohamedsalah.taskmanager.repository.RefreshTokenRepository;
import com.mohamedsalah.taskmanager.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of RefreshTokenService for token generation and validation.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${application.jwt.refresh-token-expiration}")
    private long refreshTokenExpirationMs;

    @Override
    public RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpirationMs / 1000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token is not in database!"));
        
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidTokenException("Refresh token was expired. Please make a new signin request");
        }
        return refreshToken;
    }

    @Override
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
