package com.movie_app_backend.movie_app_backend.util;

import com.movie_app_backend.movie_app_backend.model.User;
import com.movie_app_backend.movie_app_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SecurityUtil - Utility class for security-related operations
 */
@Slf4j
public class SecurityUtil {

    private SecurityUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Get current authenticated user from SecurityContext
     * @param userRepository the user repository
     * @return User entity
     * @throws RuntimeException if user not found
     */
    public static User getCurrentUser(UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("No authenticated user found in SecurityContext");
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found in database: {}", email);
                    return new RuntimeException("User not found");
                });
    }

    /**
     * Get current authenticated username
     * @return username/email
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return authentication.getName();
    }

    /**
     * Check if user is authenticated
     * @return true if authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}

