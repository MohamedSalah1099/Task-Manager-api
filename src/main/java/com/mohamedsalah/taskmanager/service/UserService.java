package com.mohamedsalah.taskmanager.service;

import com.mohamedsalah.taskmanager.dto.response.UserResponse;
import java.util.List;

/**
 * Service interface for managing users.
 */
public interface UserService {
    UserResponse getCurrentUser(String email);
    List<UserResponse> getAllUsers();
}
