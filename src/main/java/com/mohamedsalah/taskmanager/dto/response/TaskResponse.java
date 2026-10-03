package com.mohamedsalah.taskmanager.dto.response;

import com.mohamedsalah.taskmanager.enums.TaskPriority;
import com.mohamedsalah.taskmanager.enums.TaskStatus;

import java.time.LocalDateTime;

/**
 * Response payload representing full task details.
 */
public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        String category,
        LocalDateTime dueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
