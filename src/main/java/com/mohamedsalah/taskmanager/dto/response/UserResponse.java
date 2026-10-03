package com.mohamedsalah.taskmanager.dto.response;

/**
 * Response payload representing user account details.
 */
public record UserResponse(
        Long id,
        String username,
        String email,
        String role
) {
}
