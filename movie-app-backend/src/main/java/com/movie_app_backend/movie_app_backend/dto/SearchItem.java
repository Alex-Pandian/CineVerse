package com.movie_app_backend.movie_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchItem {

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("imdbID")
    private String imdbID;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Poster")
    private String poster;

    // Getters
    public String getTitle() { return title; }
    public String getYear() { return year; }
    public String getImdbID() { return imdbID; }
    public String getType() { return type; }
    public String getPoster() { return poster; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setYear(String year) { this.year = year; }
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }
    public void setType(String type) { this.type = type; }
    public void setPoster(String poster) { this.poster = poster; }
}