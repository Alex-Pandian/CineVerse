package com.movie_app_backend.movie_app_backend.service;

import com.movie_app_backend.movie_app_backend.dto.MovieSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TrendingService - Service for trending and recommendation features
 * Tracks popular movies based on search count and favorites
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TrendingService {

    private final SearchHistoryService searchHistoryService;
    private final MovieService movieService;

    /**
     * Get trending movies based on search frequency
     * @param limit number of trending movies to return
     * @return List of trending movie titles
     */
    public List<String> getTrendingMovies(int limit) {
        log.info("Fetching trending movies - Limit: {}", limit);

        try {
            List<String> trendingSearches = searchHistoryService.getTrendingSearches(limit);
            log.info("Found {} trending searches", trendingSearches.size());
            return trendingSearches;
        } catch (Exception e) {
            log.error("Error fetching trending movies", e);
            return new ArrayList<>();
        }
    }

    /**
     * Get trending movies with default limit
     * @return List of trending movies
     */
    public List<String> getTrendingMovies() {
        return getTrendingMovies(10);
    }

    /**
     * Get recommendations based on a specific genre/keyword
     * @param genre the genre or keyword
     * @param limit number of recommendations
     * @return List of MovieSearchResponse
     */
    public List<MovieSearchResponse> getRecommendationsByGenre(String genre, int limit) {
        log.info("Fetching recommendations for genre: {} - Limit: {}", genre, limit);

        try {
            List<MovieSearchResponse> recommendations = movieService.getRecommendations(genre);

            // Limit results
            return recommendations.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching recommendations for genre: {}", genre, e);
            return new ArrayList<>();
        }
    }

    /**
     * Get recommendations based on keywords with default limit
     * @param genre the genre or keyword
     * @return List of MovieSearchResponse
     */
    public List<MovieSearchResponse> getRecommendationsByGenre(String genre) {
        return getRecommendationsByGenre(genre, 10);
    }

    /**
     * Track popularity of a movie by search count
     * (In a production system, this would query a tracking table)
     * @param movieTitle the movie title
     * @return popularity score
     */
    public int getMoviePopularityScore(String movieTitle) {
        log.info("Calculating popularity score for: {}", movieTitle);

        try {
            List<String> trendingSearches = searchHistoryService.getTrendingSearches(100);

            // Count occurrences (simplified version)
            int count = (int) trendingSearches.stream()
                    .filter(s -> s.toLowerCase().contains(movieTitle.toLowerCase()))
                    .count();

            log.info("Popularity score for '{}': {}", movieTitle, count);
            return count;
        } catch (Exception e) {
            log.error("Error calculating popularity score", e);
            return 0;
        }
    }

    /**
     * Get personalized recommendations based on user's favorite genres
     * @param favoriteGenres list of genres user has favorited
     * @param limit number of recommendations
     * @return List of MovieSearchResponse
     */
    public List<MovieSearchResponse> getPersonalizedRecommendations(List<String> favoriteGenres, int limit) {
        log.info("Fetching personalized recommendations for genres: {}", favoriteGenres);

        Map<String, Integer> movieScores = new HashMap<>();

        try {
            for (String genre : favoriteGenres) {
                List<MovieSearchResponse> recommendations = movieService.getRecommendations(genre);

                for (MovieSearchResponse movie : recommendations) {
                    movieScores.put(movie.getImdbId(),
                            movieScores.getOrDefault(movie.getImdbId(), 0) + 1);
                }
            }

            // Sort by score and convert back to MovieSearchResponse
            List<MovieSearchResponse> personalizedRecs = movieScores.entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .limit(limit)
                    .map(entry -> MovieSearchResponse.builder()
                            .imdbId(entry.getKey())
                            .build())
                    .collect(Collectors.toList());

            log.info("Generated {} personalized recommendations", personalizedRecs.size());
            return personalizedRecs;
        } catch (Exception e) {
            log.error("Error generating personalized recommendations", e);
            return new ArrayList<>();
        }
    }
}

