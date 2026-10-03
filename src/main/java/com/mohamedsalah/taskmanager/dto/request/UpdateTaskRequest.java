package com.mohamedsalah.taskmanager.dto.request;

import com.mohamedsalah.taskmanager.enums.TaskPriority;
import com.mohamedsalah.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Request payload for updating an existing task with optional fields.
 */
public record UpdateTaskRequest(
        @Size(max = 200)
        String title,

        @Size(max = 2000)
        String description,

        TaskStatus status,

        TaskPriority priority,

        @Size(max = 100)
        String category,

        LocalDateTime dueDate
) {
}
