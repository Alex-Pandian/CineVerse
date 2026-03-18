package com.movie_app_backend.movie_app_backend.service;

import com.movie_app_backend.movie_app_backend.client.OmdbApiClient;
import com.movie_app_backend.movie_app_backend.dto.MovieDetailsResponse;
import com.movie_app_backend.movie_app_backend.dto.MovieSearchPageResponse;
import com.movie_app_backend.movie_app_backend.dto.MovieSearchResponse;
import com.movie_app_backend.movie_app_backend.dto.OmdbMovieResponse;
import com.movie_app_backend.movie_app_backend.dto.SearchItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * MovieService - Service for movie operations
 * Handles searching movies, fetching details, and recommendations
 */
@Service
public class MovieService {

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    private final OmdbApiClient omdbApiClient;

    public MovieService(OmdbApiClient omdbApiClient) {
        this.omdbApiClient = omdbApiClient;
    }

    /**
     * Search movies by name, year, type, and page
     * @param movieName the movie name to search
     * @param year optional year
     * @param type optional type (movie, series, episode)
     * @param page optional page number
     * @return MovieSearchPageResponse
     */
    public MovieSearchPageResponse searchMovies(String movieName, String year, String type, Integer page) {
        log.info("Searching movies by name: {}", movieName);

        List<MovieSearchResponse> results = new ArrayList<>();
        int totalResults = 0;
        int totalPages = 0;

        try {
            OmdbMovieResponse response = omdbApiClient.searchMovies(movieName, year, type, page);

            if (response == null || "False".equalsIgnoreCase(response.getResponse())) {
                log.warn("No movies found for: {}", movieName);
                String error = response != null ? response.getError() : "No response from OMDb";
                return new MovieSearchPageResponse(results, totalResults, totalPages, page != null ? page : 1, error);
            }

            // Parse search results from response
            if (response.getSearch() != null && !response.getSearch().isEmpty()) {
                for (SearchItem item : response.getSearch()) {
                    MovieSearchResponse searchResponse = MovieSearchResponse.builder()
                            .imdbId(item.getImdbID())
                            .title(item.getTitle())
                            .year(item.getYear())
                            .type(item.getType())
                            .poster(item.getPoster())
                            .build();
                    results.add(searchResponse);
                }
            }

            try {
                if (response.getTotalResults() != null) {
                    totalResults = Integer.parseInt(response.getTotalResults());
                    totalPages = (int) Math.ceil(totalResults / 10.0);
                }
            } catch (NumberFormatException ex) {
                log.warn("Unable to parse totalResults from OMDb: {}", response.getTotalResults());
            }

            log.info("Found {} movies for search query: {}", results.size(), movieName);
        } catch (Exception e) {
            log.error("Error searching movies for: {}", movieName, e);
            return new MovieSearchPageResponse(results, totalResults, totalPages, page != null ? page : 1, e.getMessage());
        }

        return new MovieSearchPageResponse(results, totalResults, totalPages, page != null ? page : 1, null);
    }

    /**
     * Fetch movie details by IMDb ID
     * @param imdbId the IMDb ID of the movie
     * @return MovieDetailsResponse with complete movie details
     */
    public MovieDetailsResponse getMovieDetails(String imdbId) {
        log.info("Fetching movie details for IMDb ID: {}", imdbId);

        try {
            OmdbMovieResponse response = omdbApiClient.getMovieDetails(imdbId);

            if (response == null || "False".equalsIgnoreCase(response.getResponse())) {
                log.warn("Movie not found for IMDb ID: {}", imdbId);
                return MovieDetailsResponse.builder()
                        .errorMessage("Movie not found with IMDb ID: " + imdbId)
                        .build();
            }

            // Map OMDb response to clean DTO
            MovieDetailsResponse movieDetails = MovieDetailsResponse.builder()
                    .imdbId(response.getImdbID())
                    .title(response.getTitle())
                    .year(response.getYear())
                    .rated(response.getRated())
                    .released(response.getReleased())
                    .runtime(response.getRuntime())
                    .genre(response.getGenre())
                    .director(response.getDirector())
                    .writer(response.getWriter())
                    .actors(response.getActors())
                    .plot(response.getPlot())
                    .language(response.getLanguage())
                    .country(response.getCountry())
                    .awards(response.getAwards())
                    .poster(response.getPoster())
                    .metascore(response.getMetascore())
                    .imdbRating(response.getImdbRating())
                    .imdbVotes(response.getImdbVotes())
                    .type(response.getType())
                    .boxOffice(response.getBoxOffice())
                    .production(response.getProduction())
                    .build();

            log.info("Successfully fetched details for movie: {}", movieDetails.getTitle());
            return movieDetails;
        } catch (Exception e) {
            log.error("Error fetching movie details for IMDb ID: {}", imdbId, e);
            return MovieDetailsResponse.builder()
                    .errorMessage("Failed to fetch movie details: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Get recommendations based on genre
     * @param genre the genre to search for
     * @return List of MovieSearchResponse
     */
    public List<MovieSearchResponse> getRecommendations(String genre) {
        log.info("Getting recommendations for genre: {}", genre);

        // Simple recommendation: search for popular movies of the genre
        List<MovieSearchResponse> recommendations = new ArrayList<>();

        try {
            // Search for popular movies in the genre
            OmdbMovieResponse response = omdbApiClient.searchMovies(genre, null, "movie", 1);

            if (response != null && !"False".equalsIgnoreCase(response.getResponse()) && response.getSearch() != null && !response.getSearch().isEmpty()) {
                for (SearchItem item : response.getSearch()) {
                    MovieSearchResponse searchResponse = MovieSearchResponse.builder()
                            .imdbId(item.getImdbID())
                            .title(item.getTitle())
                            .year(item.getYear())
                            .type(item.getType())
                            .poster(item.getPoster())
                            .build();
                    recommendations.add(searchResponse);
                }
            }

            log.info("Found {} recommendations for genre: {}", recommendations.size(), genre);
        } catch (Exception e) {
            log.error("Error getting recommendations for genre: {}", genre, e);
        }

        return recommendations;
    }
}
