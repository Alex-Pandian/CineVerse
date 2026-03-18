package com.movie_app_backend.movie_app_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * SecurityConfig - Spring Security configuration for JWT-based authentication
 * Implements modern Spring Security standards for Spring Boot 3.x
 *
 * Features:
 * - Stateless session management
 * - JWT token-based authentication
 * - CSRF protection disabled (stateless)
 * - CORS enabled for frontend communication
 * - Public access to /api/auth/** endpoints
 * - Secured access to all other endpoints
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${cors.allowed.origins:http://localhost:5173}")
    private String corsAllowedOrigins;

    @Value("${cors.allowed.methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String corsAllowedMethods;

    @Value("${cors.allowed.headers:*}")
    private String corsAllowedHeaders;

    @Value("${cors.allow.credentials:true}")
    private boolean corsAllowCredentials;

    /**
     * Configure the security filter chain
     * @param http HttpSecurity object to configure
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Disable CSRF since we're using stateless JWT authentication
                .csrf(csrf -> csrf.disable())

                // Set session creation policy to stateless (no session storage)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configure authorization rules
                .authorizeHttpRequests(authz -> authz
                        // Permit public access to authentication endpoints
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/public/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Add JWT authentication filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // Exception handling for authentication and authorization errors
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"error\": \"Unauthorized\", " +
                                    "\"message\": \"" + authException.getMessage() + "\"}"
                            );
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"error\": \"Forbidden\", " +
                                    "\"message\": \"Access denied\"}"
                            );
                        })
                );

        return http.build();
    }

    /**
     * Configure the password encoder bean
     * Uses BCrypt with strength 10 (default)
     * @return PasswordEncoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Configure the authentication manager bean
     * @param authenticationConfiguration the authentication configuration
     * @return AuthenticationManager bean
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configure CORS configuration source
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(corsAllowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList(corsAllowedMethods.split(",")));
        configuration.setAllowedHeaders(Arrays.asList(corsAllowedHeaders.split(",")));
        configuration.setAllowCredentials(corsAllowCredentials);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

