package com.movie_app_backend.movie_app_backend.util;

import com.movie_app_backend.movie_app_backend.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JwtTokenProvider - Utility class for JWT token generation and validation
 * Uses JJWT 0.12.3 API
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:mySecretKeyForJWTTokenGenerationAndValidationPurposesOnly}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}") // Default: 24 hours in milliseconds
    private long jwtExpirationInMs;

    /**
     * Generate JWT token from user
     * @param user the user object
     * @return JWT token string
     */
    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key)
                .compact();
    }

    /**
     * Get user ID from JWT token
     * @param token the JWT token
     * @return user ID
     */
    public Long getUserIdFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Long.parseLong(Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    /**
     * Validate JWT token
     * @param token the JWT token
     * @return true if token is valid
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
