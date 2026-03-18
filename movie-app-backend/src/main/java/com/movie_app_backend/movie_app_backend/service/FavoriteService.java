package com.movie_app_backend.movie_app_backend.service;

import com.movie_app_backend.movie_app_backend.dto.FavoriteResponse;
import com.movie_app_backend.movie_app_backend.model.Favorite;
import com.movie_app_backend.movie_app_backend.model.User;
import com.movie_app_backend.movie_app_backend.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FavoriteService - Service for managing user favorite movies
 * Handles add, remove, and retrieval of favorites
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Add a movie to user's favorites
     * @param user the user
     * @param imdbId the IMDb ID of the movie
     * @param title the movie title
     * @param poster the movie poster URL
     * @return FavoriteResponse
     */
    public FavoriteResponse addToFavorites(User user, String imdbId, String title, String poster) {
        log.info("Adding movie to favorites for user: {} - IMDb ID: {}", user.getEmail(), imdbId);

        // Check if already exists
        if (favoriteRepository.existsByUserAndImdbId(user, imdbId)) {
            log.warn("Movie already in favorites for user: {}", user.getEmail());
            throw new RuntimeException("Movie already in your favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setImdbId(imdbId);
        favorite.setTitle(title);
        favorite.setPoster(poster);
        favorite.setCreatedAt(LocalDateTime.now());
        favorite.setUpdatedAt(LocalDateTime.now());

        Favorite saved = favoriteRepository.save(favorite);
        log.info("Movie added to favorites successfully");

        return mapToResponse(saved);
    }

    /**
     * Remove a movie from user's favorites
     * @param user the user
     * @param imdbId the IMDb ID of the movie
     */
    public void removeFromFavorites(User user, String imdbId) {
        log.info("Removing movie from favorites for user: {} - IMDb ID: {}", user.getEmail(), imdbId);

        Favorite favorite = favoriteRepository.findByUserAndImdbId(user, imdbId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        favoriteRepository.delete(favorite);
        log.info("Movie removed from favorites successfully");
    }

    /**
     * Get all favorites for a user
     * @param user the user
     * @return List of FavoriteResponse
     */
    public List<FavoriteResponse> getUserFavorites(User user) {
        log.info("Fetching favorites for user: {}", user.getEmail());

        List<Favorite> favorites = favoriteRepository.findByUserOrderByCreatedAtDesc(user);

        log.info("Found {} favorites for user: {}", favorites.size(), user.getEmail());

        return favorites.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Check if a movie is favorited by a user
     * @param user the user
     * @param imdbId the IMDb ID
     * @return true if favorited, false otherwise
     */
    public boolean isFavorited(User user, String imdbId) {
        return favoriteRepository.existsByUserAndImdbId(user, imdbId);
    }

    /**
     * Map Favorite entity to FavoriteResponse DTO
     * @param favorite the favorite entity
     * @return FavoriteResponse
     */
    private FavoriteResponse mapToResponse(Favorite favorite) {
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .imdbId(favorite.getImdbId())
                .title(favorite.getTitle())
                .poster(favorite.getPoster())
                .createdAt(favorite.getCreatedAt().format(DATE_FORMATTER))
                .build();
    }
}

