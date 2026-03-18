package com.movie_app_backend.movie_app_backend.repository;

import com.movie_app_backend.movie_app_backend.model.SearchHistory;
import com.movie_app_backend.movie_app_backend.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SearchHistoryRepository - Data access layer for SearchHistory entities
 */
@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    /**
     * Find recent searches for a user
     * @param user the user
     * @param pageable pagination info
     * @return List of search history
     */
    List<SearchHistory> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    /**
     * Find searches within a date range
     * @param user the user
     * @param startDate start date
     * @param endDate end date
     * @return List of search history
     */
    List<SearchHistory> findByUserAndCreatedAtBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all searches for a user ordered by recency
     * @param user the user
     * @return List of search history
     */
    List<SearchHistory> findByUserOrderByCreatedAtDesc(User user);
}

