package com.mohamedsalah.taskmanager.controller;

import com.mohamedsalah.taskmanager.dto.response.TaskResponse;
import com.mohamedsalah.taskmanager.dto.response.UserResponse;
import com.mohamedsalah.taskmanager.service.TaskService;
import com.mohamedsalah.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for administrative management operations.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administration")
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/tasks")
    public ResponseEntity<Page<TaskResponse>> getAllTasks(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.adminDeleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
