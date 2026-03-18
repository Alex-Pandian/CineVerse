package com.movie_app_backend.movie_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * MovieDetailsResponse - Clean DTO for movie details
 * Returns only relevant fields from OMDb API
 */
public class MovieDetailsResponse {

    @JsonProperty("imdbID")
    private String imdbId;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Rated")
    private String rated;

    @JsonProperty("Released")
    private String released;

    @JsonProperty("Runtime")
    private String runtime;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Director")
    private String director;

    @JsonProperty("Writer")
    private String writer;

    @JsonProperty("Actors")
    private String actors;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Awards")
    private String awards;

    @JsonProperty("Poster")
    private String poster;

    @JsonProperty("Metascore")
    private String metascore;

    @JsonProperty("imdbRating")
    private String imdbRating;

    @JsonProperty("imdbVotes")
    private String imdbVotes;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("BoxOffice")
    private String boxOffice;

    @JsonProperty("Production")
    private String production;

    @JsonProperty("Response")
    private String response;

    @JsonProperty("Error")
    private String errorMessage;

    // Private constructor for builder
    private MovieDetailsResponse(String imdbId, String title, String year, String rated, String released,
                                String runtime, String genre, String director, String writer, String actors,
                                String plot, String language, String country, String awards, String poster,
                                String metascore, String imdbRating, String imdbVotes, String type,
                                String boxOffice, String production, String errorMessage) {
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.language = language;
        this.country = country;
        this.awards = awards;
        this.poster = poster;
        this.metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.type = type;
        this.boxOffice = boxOffice;
        this.production = production;
        this.errorMessage = errorMessage;
    }

    // Getters
    public String getImdbId() { return imdbId; }
    public String getTitle() { return title; }
    public String getYear() { return year; }
    public String getRated() { return rated; }
    public String getReleased() { return released; }
    public String getRuntime() { return runtime; }
    public String getGenre() { return genre; }
    public String getDirector() { return director; }
    public String getWriter() { return writer; }
    public String getActors() { return actors; }
    public String getPlot() { return plot; }
    public String getLanguage() { return language; }
    public String getCountry() { return country; }
    public String getAwards() { return awards; }
    public String getPoster() { return poster; }
    public String getMetascore() { return metascore; }
    public String getImdbRating() { return imdbRating; }
    public String getImdbVotes() { return imdbVotes; }
    public String getType() { return type; }
    public String getBoxOffice() { return boxOffice; }
    public String getProduction() { return production; }
    public String getErrorMessage() { return errorMessage; }

    /**
     * Builder class for MovieDetailsResponse
     */
    public static class Builder {
        private String imdbId;
        private String title;
        private String year;
        private String rated;
        private String released;
        private String runtime;
        private String genre;
        private String director;
        private String writer;
        private String actors;
        private String plot;



        private String language;
        private String country;
        private String awards;
        private String poster;
        private String metascore;
        private String imdbRating;
        private String imdbVotes;
        private String type;
        private String boxOffice;
        private String production;
        private String errorMessage;

        public Builder imdbId(String imdbId) { this.imdbId = imdbId; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder year(String year) { this.year = year; return this; }
        public Builder rated(String rated) { this.rated = rated; return this; }
        public Builder released(String released) { this.released = released; return this; }
        public Builder runtime(String runtime) { this.runtime = runtime; return this; }
        public Builder genre(String genre) { this.genre = genre; return this; }
        public Builder director(String director) { this.director = director; return this; }
        public Builder writer(String writer) { this.writer = writer; return this; }
        public Builder actors(String actors) { this.actors = actors; return this; }
        public Builder plot(String plot) { this.plot = plot; return this; }
        public Builder language(String language) { this.language = language; return this; }
        public Builder country(String country) { this.country = country; return this; }
        public Builder awards(String awards) { this.awards = awards; return this; }
        public Builder poster(String poster) { this.poster = poster; return this; }
        public Builder metascore(String metascore) { this.metascore = metascore; return this; }
        public Builder imdbRating(String imdbRating) { this.imdbRating = imdbRating; return this; }
        public Builder imdbVotes(String imdbVotes) { this.imdbVotes = imdbVotes; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder boxOffice(String boxOffice) { this.boxOffice = boxOffice; return this; }
        public Builder production(String production) { this.production = production; return this; }
        public Builder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }

        public MovieDetailsResponse build() {
            return new MovieDetailsResponse(imdbId, title, year, rated, released, runtime, genre,
                                          director, writer, actors, plot, language, country, awards,
                                          poster, metascore, imdbRating, imdbVotes, type, boxOffice,
                                          production, errorMessage);
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
