package com.movie_app_backend.movie_app_backend.controller;

import com.movie_app_backend.movie_app_backend.dto.FavoriteResponse;
import com.movie_app_backend.movie_app_backend.model.User;
import com.movie_app_backend.movie_app_backend.repository.UserRepository;
import com.movie_app_backend.movie_app_backend.service.FavoriteService;
import com.movie_app_backend.movie_app_backend.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * FavoriteController - REST controller for user favorite movies
 * Handles adding, removing, and retrieving favorite movies
 * All endpoints require authentication
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private static final Logger log = LoggerFactory.getLogger(FavoriteController.class);
    private final FavoriteService favoriteService;
    private final UserRepository userRepository;

    public FavoriteController(FavoriteService favoriteService, UserRepository userRepository) {
        this.favoriteService = favoriteService;
        this.userRepository = userRepository;
    }

    /**
     * Add a movie to user's favorites
     * @param imdbId the IMDb ID of the movie
     * @param title the movie title
     * @param poster the movie poster URL
     * @return FavoriteResponse
     */
    @PostMapping("/{imdbId}")
    public ResponseEntity<FavoriteResponse> addToFavorites(
            @PathVariable String imdbId,
            @RequestParam String title,
            @RequestParam(required = false) String poster) {

        log.info("Adding movie to favorites - IMDb ID: {}, Title: {}", imdbId, title);

        User user = SecurityUtil.getCurrentUser(userRepository);
        FavoriteResponse response = favoriteService.addToFavorites(user, imdbId, title, poster);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all favorites for the current user
     * @return List of FavoriteResponse
     */
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getUserFavorites() {
        log.info("Fetching user favorites");

        User user = SecurityUtil.getCurrentUser(userRepository);
        List<FavoriteResponse> favorites = favoriteService.getUserFavorites(user);

        return ResponseEntity.ok(favorites);
    }

    /**
     * Remove a movie from user's favorites
     * @param imdbId the IMDb ID of the movie
     * @return no content response
     */
    @DeleteMapping("/{imdbId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable String imdbId) {
        log.info("Removing movie from favorites - IMDb ID: {}", imdbId);

        User user = SecurityUtil.getCurrentUser(userRepository);
        favoriteService.removeFromFavorites(user, imdbId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Check if a movie is favorited by the user
     * @param imdbId the IMDb ID of the movie
     * @return true/false
     */
    @GetMapping("/{imdbId}/check")
    public ResponseEntity<Boolean> isFavorited(@PathVariable String imdbId) {
        log.info("Checking if movie is favorited - IMDb ID: {}", imdbId);

        User user = SecurityUtil.getCurrentUser(userRepository);
        boolean isFavorited = favoriteService.isFavorited(user, imdbId);

        return ResponseEntity.ok(isFavorited);
    }
}
