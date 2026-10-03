package com.mohamedsalah.taskmanager.service.impl;

import com.mohamedsalah.taskmanager.dto.request.LoginRequest;
import com.mohamedsalah.taskmanager.dto.request.RefreshTokenRequest;
import com.mohamedsalah.taskmanager.dto.request.RegisterRequest;
import com.mohamedsalah.taskmanager.dto.response.AuthenticationResponse;
import com.mohamedsalah.taskmanager.dto.response.UserResponse;
import com.mohamedsalah.taskmanager.exception.EmailAlreadyExistsException;
import com.mohamedsalah.taskmanager.exception.UnauthorizedException;
import com.mohamedsalah.taskmanager.exception.UserNotFoundException;
import com.mohamedsalah.taskmanager.exception.UsernameAlreadyExistsException;
import com.mohamedsalah.taskmanager.mapper.UserMapper;
import com.mohamedsalah.taskmanager.entity.RefreshToken;
import com.mohamedsalah.taskmanager.entity.Role;
import com.mohamedsalah.taskmanager.entity.User;
import com.mohamedsalah.taskmanager.enums.RoleType;
import com.mohamedsalah.taskmanager.repository.RoleRepository;
import com.mohamedsalah.taskmanager.repository.UserRepository;
import com.mohamedsalah.taskmanager.security.JwtService;
import com.mohamedsalah.taskmanager.service.AuthenticationService;
import com.mohamedsalah.taskmanager.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Implementation of AuthenticationService handling registration, login, and token processing.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }

        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .role(userRole)
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid credentials");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Map<String, Object> extraClaims = Map.of("role", user.getRole().getName().name());
        String accessToken = jwtService.generateToken(extraClaims, userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(jwtService.getAccessTokenExpirationMs() / 1000)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken validToken = refreshTokenService.validateRefreshToken(request.refreshToken());
        User user = validToken.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        
        Map<String, Object> extraClaims = Map.of("role", user.getRole().getName().name());
        String accessToken = jwtService.generateToken(extraClaims, userDetails);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(validToken.getToken())
                .expiresIn(jwtService.getAccessTokenExpirationMs() / 1000)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        refreshTokenService.deleteByUser(user);
    }
}
