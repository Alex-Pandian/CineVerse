package com.movie_app_backend.movie_app_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfig - Application configuration class
 * Defines beans for general application configurations
 *
 * Note: Security-specific beans (PasswordEncoder, etc.) are defined in SecurityConfig
 */
@Configuration
public class AppConfig {

    /**
     * Configure RestTemplate bean for external API calls
     * @param builder RestTemplateBuilder
     * @return RestTemplate bean
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    /**
     * Configure ObjectMapper bean for JSON processing
     * Includes JSR310 module for Java 8 date/time types
     * @return ObjectMapper bean
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
