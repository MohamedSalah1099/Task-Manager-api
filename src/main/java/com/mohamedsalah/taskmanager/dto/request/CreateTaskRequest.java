package com.mohamedsalah.taskmanager.dto.request;

import com.mohamedsalah.taskmanager.enums.TaskPriority;
import com.mohamedsalah.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Request payload for creating a new task.
 */
public record CreateTaskRequest(
        @NotBlank
        @Size(max = 200)
        String title,

        @Size(max = 2000)
        String description,

        @NotNull
        TaskStatus status,

        @NotNull
        TaskPriority priority,

        @Size(max = 100)
        String category,

        LocalDateTime dueDate
) {
}
