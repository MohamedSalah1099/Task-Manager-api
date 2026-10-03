package com.mohamedsalah.taskmanager.controller;

import com.mohamedsalah.taskmanager.dto.request.CreateTaskRequest;
import com.mohamedsalah.taskmanager.dto.request.UpdateTaskRequest;
import com.mohamedsalah.taskmanager.dto.response.TaskResponse;
import com.mohamedsalah.taskmanager.enums.TaskPriority;
import com.mohamedsalah.taskmanager.enums.TaskStatus;
import com.mohamedsalah.taskmanager.security.SecurityUtils;
import com.mohamedsalah.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller providing task management operations for authenticated users.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        String email = SecurityUtils.getCurrentUsername();
        TaskResponse response = taskService.createTask(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getUserTasks(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        String email = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(taskService.getUserTasks(email, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        String email = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(taskService.getTaskById(id, email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        String email = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(taskService.updateTask(id, request, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        String email = SecurityUtils.getCurrentUsername();
        taskService.deleteTask(id, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TaskResponse>> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        String email = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(taskService.searchTasks(email, title, category, pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<TaskResponse>> filterTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        String email = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(taskService.filterTasks(email, status, priority, category, pageable));
    }
}
