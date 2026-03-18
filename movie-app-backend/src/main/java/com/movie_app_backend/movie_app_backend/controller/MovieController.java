package com.movie_app_backend.movie_app_backend.controller;

import com.movie_app_backend.movie_app_backend.dto.MovieDetailsResponse;
import com.movie_app_backend.movie_app_backend.dto.MovieSearchPageResponse;
import com.movie_app_backend.movie_app_backend.service.MovieService;
import com.movie_app_backend.movie_app_backend.service.SearchHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MovieController - REST controller for movie operations
 * Handles searching movies and fetching details
 */
@RestController
@RequestMapping("/api/movies")
public class  MovieController {

    private static final Logger log = LoggerFactory.getLogger(MovieController.class);
    private final MovieService movieService;
    private final SearchHistoryService searchHistoryService;

    public MovieController(MovieService movieService, SearchHistoryService searchHistoryService) {
        this.movieService = movieService;
        this.searchHistoryService = searchHistoryService;
    }

    /**
     * Search movies by name, year, type, and page
     * @param name the movie name (required)
     * @param year optional year
     * @param type optional type (movie, series, episode)
     * @param page optional page number
     * @return MovieSearchPageResponse
     */
    @GetMapping("/search")
    public ResponseEntity<MovieSearchPageResponse> searchMovies(
            @RequestParam String name,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "1") Integer page) {

        log.info("Searching movies - Name: {}, Year: {}, Type: {}, Page: {}", name, year, type, page);

        MovieSearchPageResponse results = movieService.searchMovies(name, year, type, page);

        // Record search in history if user is authenticated
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                    // In a real scenario, you'd fetch the actual User entity here
                    // For now, we'll skip recording for unauthenticated search results
                }
            }
        } catch (Exception e) {
            log.debug("Could not record search history", e);
        }

        return ResponseEntity.ok(results);
    }

    /**
     * Get movie details by IMDb ID
     * @param imdbId the IMDb ID of the movie
     * @return MovieDetailsResponse with complete details
     */
    @GetMapping("/{imdbId}")
    public ResponseEntity<MovieDetailsResponse> getMovieDetails(@PathVariable String imdbId) {
        log.info("Fetching movie details for IMDb ID: {}", imdbId);

        MovieDetailsResponse movie = movieService.getMovieDetails(imdbId);

        if (movie.getErrorMessage() != null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(movie);
    }

    /**
     * Search movies by name (legacy endpoint for backward compatibility)
     * @param name the movie name (required)
     * @param year optional year
     * @param type optional type (movie, series, episode)
     * @param page optional page number
     * @return MovieSearchPageResponse
     */
    @GetMapping
    public ResponseEntity<MovieSearchPageResponse> searchMoviesLegacy(
            @RequestParam String name,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "1") Integer page) {

        log.info("Legacy search endpoint called - redirecting to search method");

        return searchMovies(name, year, type, page);
    }
}
