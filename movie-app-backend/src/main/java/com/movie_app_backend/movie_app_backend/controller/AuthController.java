package com.movie_app_backend.movie_app_backend.controller;

import com.movie_app_backend.movie_app_backend.dto.AuthResponse;
import com.movie_app_backend.movie_app_backend.dto.LoginRequest;
import com.movie_app_backend.movie_app_backend.dto.RegisterRequest;
import com.movie_app_backend.movie_app_backend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController - REST controller for authentication endpoints
 * Handles user registration and login requests
 * All endpoints are public and do not require authentication
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user
     * @param request RegisterRequest containing name, email, and password
     * @return ResponseEntity with AuthResponse containing user details
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        log.info("Processing registration request for email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticate user and return JWT token
     * @param request LoginRequest containing email and password
     * @return ResponseEntity with AuthResponse containing JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        log.info("Processing login request for email: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
