package com.mohamedsalah.taskmanager.dto.response;

import java.time.LocalDateTime;

/**
 * Generic response payload for operational messages.
 */
public record MessageResponse(
        String message,
        LocalDateTime timestamp
) {
}
