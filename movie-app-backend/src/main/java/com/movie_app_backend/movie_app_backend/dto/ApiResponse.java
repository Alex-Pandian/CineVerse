package com.movie_app_backend.movie_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ApiResponse - Generic API response wrapper
 * Provides consistent response format across all endpoints
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private long timestamp;

    // Private constructor for builder
    private ApiResponse(boolean success, String message, T data, String errorCode, long timestamp) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    // Default constructor
    public ApiResponse() {}

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Create a successful response
     * @param message response message
     * @param data response data
     * @param <T> data type
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null, System.currentTimeMillis());
    }

    /**
     * Create a successful response without data
     * @param message response message
     * @param <T> data type
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(String message) {
        return success(message, null);
    }

    /**
     * Create an error response
     * @param message error message
     * @param errorCode error code
     * @param <T> data type
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(false, message, null, errorCode, System.currentTimeMillis());
    }

    /**
     * Builder class for ApiResponse
     */
    public static class Builder<T> {
        private boolean success;
        private String message;
        private T data;
        private String errorCode;
        private long timestamp;

        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder<T> timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(success, message, data, errorCode, timestamp);
        }
    }

    /**
     * Create a new builder
     * @param <T> data type
     * @return Builder instance
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
}
