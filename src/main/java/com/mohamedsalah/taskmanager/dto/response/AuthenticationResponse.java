package com.mohamedsalah.taskmanager.dto.response;

import lombok.Builder;

/**
 * Response payload containing authentication tokens and validity duration.
 */
@Builder
public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        String tokenType
) {
}
