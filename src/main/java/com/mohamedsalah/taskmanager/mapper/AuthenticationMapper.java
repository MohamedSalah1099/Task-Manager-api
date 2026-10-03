package com.mohamedsalah.taskmanager.mapper;

import com.mohamedsalah.taskmanager.dto.response.AuthenticationResponse;
import org.mapstruct.Mapper;

/**
 * Mapper component for constructing authentication response DTOs.
 */
@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    default AuthenticationResponse toAuthenticationResponse(String accessToken, String refreshToken, long expiresInSeconds) {
        return new AuthenticationResponse(accessToken, refreshToken, expiresInSeconds, "Bearer");
    }
}
