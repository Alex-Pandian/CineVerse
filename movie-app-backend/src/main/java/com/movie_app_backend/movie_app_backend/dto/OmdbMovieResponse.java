package com.movie_app_backend.movie_app_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * OMDb API Response DTO
 * Represents raw response from OMDb API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbMovieResponse {

    @JsonProperty("imdbID")
    private String imdbID;

    @JsonProperty("Title")
    private String Title;

    @JsonProperty("Year")
    private String Year;

    @JsonProperty("Rated")
    private String Rated;

    @JsonProperty("Released")
    private String Released;

    @JsonProperty("Runtime")
    private String Runtime;

    @JsonProperty("Genre")
    private String Genre;

    @JsonProperty("Director")
    private String Director;

    @JsonProperty("Writer")
    private String Writer;

    @JsonProperty("Actors")
    private String Actors;

    @JsonProperty("Plot")
    private String Plot;

    @JsonProperty("Language")
    private String Language;

    @JsonProperty("Country")
    private String Country;

    @JsonProperty("Awards")
    private String Awards;

    @JsonProperty("Poster")
    private String Poster;

    @JsonProperty("Ratings")
    private List<Rating> Ratings;

    @JsonProperty("Metascore")
    private String Metascore;

    @JsonProperty("imdbRating")
    private String imdbRating;

    @JsonProperty("imdbVotes")
    private String imdbVotes;

    @JsonProperty("Type")
    private String Type;

    @JsonProperty("DVD")
    private String DVD;

    @JsonProperty("BoxOffice")
    private String BoxOffice;

    @JsonProperty("Production")
    private String Production;

    @JsonProperty("Website")
    private String Website;

    @JsonProperty("Response")
    private String Response;

    @JsonProperty("Error")
    private String Error;

    @JsonProperty("totalResults")
    private String totalResults;

    @JsonProperty("Search")
    private List<SearchItem> Search;

    // Default constructor
    public OmdbMovieResponse() {}

    // Getters
    public String getImdbID() { return imdbID; }
    public String getTitle() { return Title; }
    public String getYear() { return Year; }
    public String getRated() { return Rated; }
    public String getReleased() { return Released; }
    public String getRuntime() { return Runtime; }
    public String getGenre() { return Genre; }
    public String getDirector() { return Director; }
    public String getWriter() { return Writer; }
    public String getActors() { return Actors; }
    public String getPlot() { return Plot; }
    public String getLanguage() { return Language; }
    public String getCountry() { return Country; }
    public String getAwards() { return Awards; }
    public String getPoster() { return Poster; }
    public List<Rating> getRatings() { return Ratings; }
    public String getMetascore() { return Metascore; }
    public String getImdbRating() { return imdbRating; }
    public String getImdbVotes() { return imdbVotes; }
    public String getType() { return Type; }
    public String getDVD() { return DVD; }
    public String getBoxOffice() { return BoxOffice; }
    public String getProduction() { return Production; }
    public String getWebsite() { return Website; }
    public String getResponse() { return Response; }
    public String getError() { return Error; }
    public String getTotalResults() { return totalResults; }
    public List<SearchItem> getSearch() { return Search; }

    // Setters
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }
    public void setTitle(String Title) { this.Title = Title; }
    public void setYear(String Year) { this.Year = Year; }
    public void setRated(String Rated) { this.Rated = Rated; }
    public void setReleased(String Released) { this.Released = Released; }
    public void setRuntime(String Runtime) { this.Runtime = Runtime; }
    public void setGenre(String Genre) { this.Genre = Genre; }
    public void setDirector(String Director) { this.Director = Director; }
    public void setWriter(String Writer) { this.Writer = Writer; }
    public void setActors(String Actors) { this.Actors = Actors; }
    public void setPlot(String Plot) { this.Plot = Plot; }
    public void setLanguage(String Language) { this.Language = Language; }
    public void setCountry(String Country) { this.Country = Country; }
    public void setAwards(String Awards) { this.Awards = Awards; }
    public void setPoster(String Poster) { this.Poster = Poster; }
    public void setRatings(List<Rating> Ratings) { this.Ratings = Ratings; }
    public void setMetascore(String Metascore) { this.Metascore = Metascore; }
    public void setImdbRating(String imdbRating) { this.imdbRating = imdbRating; }
    public void setImdbVotes(String imdbVotes) { this.imdbVotes = imdbVotes; }
    public void setType(String Type) { this.Type = Type; }
    public void setDVD(String DVD) { this.DVD = DVD; }
    public void setBoxOffice(String BoxOffice) { this.BoxOffice = BoxOffice; }
    public void setProduction(String Production) { this.Production = Production; }
    public void setWebsite(String Website) { this.Website = Website; }
    public void setResponse(String Response) { this.Response = Response; }
    public void setError(String Error) { this.Error = Error; }
    public void setTotalResults(String totalResults) { this.totalResults = totalResults; }
    public void setSearch(List<SearchItem> Search) { this.Search = Search; }
}
