package com.mohamedsalah.taskmanager.service.impl;

import com.mohamedsalah.taskmanager.dto.request.RegisterRequest;
import com.mohamedsalah.taskmanager.dto.response.UserResponse;
import com.mohamedsalah.taskmanager.entity.Role;
import com.mohamedsalah.taskmanager.entity.User;
import com.mohamedsalah.taskmanager.enums.RoleType;
import com.mohamedsalah.taskmanager.exception.EmailAlreadyExistsException;
import com.mohamedsalah.taskmanager.exception.UsernameAlreadyExistsException;
import com.mohamedsalah.taskmanager.mapper.UserMapper;
import com.mohamedsalah.taskmanager.repository.RoleRepository;
import com.mohamedsalah.taskmanager.repository.UserRepository;
import com.mohamedsalah.taskmanager.security.JwtService;
import com.mohamedsalah.taskmanager.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for AuthenticationServiceImpl registration workflow.
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    @DisplayName("Should successfully register a new user when details are valid")
    void shouldRegisterUser() {
        RegisterRequest request = new RegisterRequest("john", "john@example.com", "Password123!");
        Role userRole = Role.builder().id(1L).name(RoleType.ROLE_USER).build();
        User savedUser = User.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .password("encodedPassword")
                .enabled(true)
                .role(userRole)
                .build();
        UserResponse expectedResponse = new UserResponse(1L, "john", "john@example.com", "ROLE_USER");

        given(userRepository.existsByEmail(request.email())).willReturn(false);
        given(userRepository.existsByUsername(request.username())).willReturn(false);
        given(roleRepository.findByName(RoleType.ROLE_USER)).willReturn(Optional.of(userRole));
        given(passwordEncoder.encode(request.password())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(savedUser);
        given(userMapper.toResponse(savedUser)).willReturn(expectedResponse);

        UserResponse actualResponse = authenticationService.register(request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.username(), actualResponse.username());
        assertEquals(expectedResponse.email(), actualResponse.email());
        verify(passwordEncoder).encode("Password123!");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email is already registered")
    void shouldThrowWhenEmailExists() {
        RegisterRequest request = new RegisterRequest("john", "john@example.com", "Password123!");

        given(userRepository.existsByEmail(request.email())).willReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> authenticationService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UsernameAlreadyExistsException when username is already in use")
    void shouldThrowWhenUsernameExists() {
        RegisterRequest request = new RegisterRequest("john", "john@example.com", "Password123!");

        given(userRepository.existsByEmail(request.email())).willReturn(false);
        given(userRepository.existsByUsername(request.username())).willReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> authenticationService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }
}
