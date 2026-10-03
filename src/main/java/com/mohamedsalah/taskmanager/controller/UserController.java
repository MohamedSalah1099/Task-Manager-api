package com.mohamedsalah.taskmanager.controller;

import com.mohamedsalah.taskmanager.dto.response.UserResponse;
import com.mohamedsalah.taskmanager.security.SecurityUtils;
import com.mohamedsalah.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authenticated user profile operations.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        String email = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(userService.getCurrentUser(email));
    }
}
