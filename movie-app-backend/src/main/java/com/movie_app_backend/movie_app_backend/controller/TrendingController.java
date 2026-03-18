package com.movie_app_backend.movie_app_backend.controller;

import com.movie_app_backend.movie_app_backend.dto.MovieSearchResponse;
import com.movie_app_backend.movie_app_backend.service.TrendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TrendingController - REST controller for trending and recommendation features
 * Handles trending movies and personalized recommendations
 */
@RestController
@RequestMapping("/api")
public class TrendingController {

    private static final Logger log = LoggerFactory.getLogger(TrendingController.class);
    private final TrendingService trendingService;

    public TrendingController(TrendingService trendingService) {
        this.trendingService = trendingService;
    }

    /**
     * Get trending movies
     * @param limit the number of trending movies (default 10)
     * @return List of trending movie titles
     */
    @GetMapping("/trending")
    public ResponseEntity<List<String>> getTrendingMovies(
            @RequestParam(required = false, defaultValue = "10") int limit) {

        log.info("Fetching trending movies - Limit: {}", limit);

        List<String> trendingMovies = trendingService.getTrendingMovies(limit);

        return ResponseEntity.ok(trendingMovies);
    }

    /**
     * Get recommendations based on genre/keyword
     * @param genre the genre or keyword (default: action)
     * @param limit the number of recommendations (default 10)
     * @return List of MovieSearchResponse
     */
    @GetMapping("/recommendations")
    public ResponseEntity<List<MovieSearchResponse>> getRecommendations(
            @RequestParam(required = false, defaultValue = "action") String genre,
            @RequestParam(required = false, defaultValue = "10") int limit) {

        log.info("Fetching recommendations for genre: {} - Limit: {}", genre, limit);

        List<MovieSearchResponse> recommendations = trendingService.getRecommendationsByGenre(genre, limit);

        return ResponseEntity.ok(recommendations);
    }

    /**
     * Get popularity score for a movie
     * @param movieTitle the movie title
     * @return popularity score
     */
    @GetMapping("/trending/popularity")
    public ResponseEntity<Integer> getMoviePopularity(@RequestParam String movieTitle) {
        log.info("Fetching popularity score for: {}", movieTitle);

        int popularityScore = trendingService.getMoviePopularityScore(movieTitle);

        return ResponseEntity.ok(popularityScore);
    }
}
