package com.movie_app_backend.movie_app_backend.repository;

import com.movie_app_backend.movie_app_backend.model.Favorite;
import com.movie_app_backend.movie_app_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * FavoriteRepository - Data access layer for Favorite entities
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    /**
     * Find all favorites for a specific user
     * @param user the user
     * @return List of favorites
     */
    List<Favorite> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Find a favorite by user and IMDb ID
     * @param user the user
     * @param imdbId the IMDb ID
     * @return Optional containing the favorite if exists
     */
    Optional<Favorite> findByUserAndImdbId(User user, String imdbId);

    /**
     * Check if a movie is favorited by a user
     * @param user the user
     * @param imdbId the IMDb ID
     * @return true if favorited, false otherwise
     */
    boolean existsByUserAndImdbId(User user, String imdbId);
}

