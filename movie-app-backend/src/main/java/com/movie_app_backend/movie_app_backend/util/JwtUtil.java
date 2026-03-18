package com.movie_app_backend.movie_app_backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * JwtUtil - Utility class for JWT token generation, validation, and extraction
 * Provides methods to work with JWT tokens in a Spring Security context
 * Uses JJWT 0.12.3 API
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret:mySecretKeyForJWTTokenGenerationAndValidationPurposesOnlyChangeInProduction}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationInMs;

    /**
     * Get the secret key for JWT signing
     * @return SecretKey for HMAC-SHA512
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generate JWT token from username
     * @param username the username to encode in the token
     * @return JWT token string
     */
    public String generateToken(String username) {
        return createToken(username, jwtExpirationInMs);
    }

    /**
     * Create JWT token with expiration
     * @param subject the subject (username) to encode
     * @param expirationTime time until expiration in milliseconds
     * @return JWT token string
     */
    private String createToken(String subject, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        try {
            return Jwts.builder()
                    .subject(subject)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSigningKey())
                    .compact();
        } catch (Exception e) {
            log.error("Error generating JWT token: {}", e.getMessage());
            throw new RuntimeException("Could not generate JWT token", e);
        }
    }

    /**
     * Extract username from JWT token
     * @param token the JWT token
     * @return username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from JWT token
     * @param token the JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a specific claim from JWT token
     * @param token the JWT token
     * @param claimsResolver function to resolve the claim
     * @param <T> type of the claim
     * @return the resolved claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from JWT token
     * @param token the JWT token
     * @return Claims object containing all claims
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Error extracting claims from JWT token: {}", e.getMessage());
            throw new RuntimeException("Could not extract claims from token", e);
        }
    }

    /**
     * Check if JWT token is expired
     * @param token the JWT token
     * @return true if token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            log.debug("Token is invalid or expired: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Validate JWT token
     * Checks signature, expiration, and format
     * @param token the JWT token
     * @return true if token is valid, false otherwise
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return !isTokenExpired(token);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
