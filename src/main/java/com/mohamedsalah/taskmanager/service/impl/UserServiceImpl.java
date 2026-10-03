package com.mohamedsalah.taskmanager.service.impl;

import com.mohamedsalah.taskmanager.dto.response.UserResponse;
import com.mohamedsalah.taskmanager.exception.UserNotFoundException;
import com.mohamedsalah.taskmanager.mapper.UserMapper;
import com.mohamedsalah.taskmanager.repository.UserRepository;
import com.mohamedsalah.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of UserService for retrieving user data.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }
}
