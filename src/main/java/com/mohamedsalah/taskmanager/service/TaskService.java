package com.mohamedsalah.taskmanager.service;

import com.mohamedsalah.taskmanager.dto.request.CreateTaskRequest;
import com.mohamedsalah.taskmanager.dto.request.UpdateTaskRequest;
import com.mohamedsalah.taskmanager.dto.response.TaskResponse;
import com.mohamedsalah.taskmanager.enums.TaskPriority;
import com.mohamedsalah.taskmanager.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for task management.
 */
public interface TaskService {
    TaskResponse createTask(CreateTaskRequest request, String email);
    TaskResponse getTaskById(Long id, String email);
    Page<TaskResponse> getUserTasks(String email, Pageable pageable);
    TaskResponse updateTask(Long id, UpdateTaskRequest request, String email);
    void deleteTask(Long id, String email);
    Page<TaskResponse> searchTasks(String email, String title, String category, Pageable pageable);
    Page<TaskResponse> filterTasks(String email, TaskStatus status, TaskPriority priority, String category, Pageable pageable);
    Page<TaskResponse> getAllTasks(Pageable pageable);
    void adminDeleteTask(Long id);
}
