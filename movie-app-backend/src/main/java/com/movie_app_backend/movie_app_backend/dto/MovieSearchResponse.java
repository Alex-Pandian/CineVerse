package com.movie_app_backend.movie_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * MovieSearchResponse - Clean DTO for movie search results
 * Does not expose raw API response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieSearchResponse {

    private String imdbId;
    private String title;
    private String year;
    private String type;
    private String poster;
    private String totalResults;
    private String errorMessage;

    // Private constructor for builder
    private MovieSearchResponse(String imdbId, String title, String year, String type, String poster, String totalResults, String errorMessage) {
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.type = type;
        this.poster = poster;
        this.totalResults = totalResults;
        this.errorMessage = errorMessage;
    }

    // Default constructor
    public MovieSearchResponse() {}

    // Getters
    public String getImdbId() {
        return imdbId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getType() {
        return type;
    }

    public String getPoster() {
        return poster;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // Setters
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Builder class for MovieSearchResponse
     */
    public static class Builder {
        private String imdbId;
        private String title;
        private String year;
        private String type;
        private String poster;
        private String totalResults;
        private String errorMessage;

        public Builder imdbId(String imdbId) {
            this.imdbId = imdbId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder year(String year) {
            this.year = year;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder poster(String poster) {
            this.poster = poster;
            return this;
        }

        public Builder totalResults(String totalResults) {
            this.totalResults = totalResults;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public MovieSearchResponse build() {
            return new MovieSearchResponse(imdbId, title, year, type, poster, totalResults, errorMessage);
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
