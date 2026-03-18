package com.movie_app_backend.movie_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Rating - DTO for movie ratings from OMDb API
 */
public class Rating {

    @JsonProperty("Source")
    private String source;

    @JsonProperty("Value")
    private String value;

    // Default constructor
    public Rating() {}

    // Getters
    public String getSource() { return source; }
    public String getValue() { return value; }

    // Setters
    public void setSource(String source) { this.source = source; }
    public void setValue(String value) { this.value = value; }
}
