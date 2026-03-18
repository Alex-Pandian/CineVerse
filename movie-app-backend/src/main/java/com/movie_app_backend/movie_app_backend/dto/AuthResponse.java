package com.movie_app_backend.movie_app_backend.dto;

/**
 * AuthResponse - DTO for authentication response containing token and user details
 */
public class AuthResponse {

    private String token;
    private Long userId;
    private String name;
    private String email;
    private String message;

    // Default constructor
    public AuthResponse() {}

    // Constructor
    public AuthResponse(String token, Long userId, String name, String email, String message) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.message = message;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Builder class for AuthResponse
     */
    public static class Builder {
        private String token;
        private Long userId;
        private String name;
        private String email;
        private String message;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(token, userId, name, email, message);
        }
    }

    /**
     * Create a new builder
     * @return Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
}
