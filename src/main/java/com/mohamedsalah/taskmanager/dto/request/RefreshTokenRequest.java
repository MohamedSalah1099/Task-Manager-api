package com.mohamedsalah.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for refreshing an access token.
 */
public record RefreshTokenRequest(
        @NotBlank
        String refreshToken
) {
}
