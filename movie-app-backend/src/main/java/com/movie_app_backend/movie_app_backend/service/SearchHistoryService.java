package com.movie_app_backend.movie_app_backend.service;

import com.movie_app_backend.movie_app_backend.dto.SearchHistoryResponse;
import com.movie_app_backend.movie_app_backend.model.SearchHistory;
import com.movie_app_backend.movie_app_backend.model.User;
import com.movie_app_backend.movie_app_backend.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SearchHistoryService - Service for managing user search history
 * Tracks searches for trending and recommendation features
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int DEFAULT_HISTORY_LIMIT = 50;

    /**
     * Record a search query for a user
     * @param user the user
     * @param query the search query
     * @return SearchHistoryResponse
     */
    public SearchHistoryResponse recordSearch(User user, String query) {
        log.info("Recording search for user: {} - Query: {}", user.getEmail(), query);

        SearchHistory history = new SearchHistory();
        history.setUser(user);
        history.setQuery(query);
        history.setCreatedAt(LocalDateTime.now());

        SearchHistory saved = searchHistoryRepository.save(history);
        log.info("Search recorded successfully");

        return mapToResponse(saved);
    }

    /**
     * Get recent search history for a user
     * @param user the user
     * @param limit the number of results to return
     * @return List of SearchHistoryResponse
     */
    public List<SearchHistoryResponse> getUserSearchHistory(User user, int limit) {
        log.info("Fetching search history for user: {} - Limit: {}", user.getEmail(), limit);

        Pageable pageable = PageRequest.of(0, limit);
        List<SearchHistory> history = searchHistoryRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        log.info("Found {} search records for user: {}", history.size(), user.getEmail());

        return history.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get recent search history for a user (default limit)
     * @param user the user
     * @return List of SearchHistoryResponse
     */
    public List<SearchHistoryResponse> getUserSearchHistory(User user) {
        return getUserSearchHistory(user, DEFAULT_HISTORY_LIMIT);
    }

    /**
     * Get searches within a specific date range
     * @param user the user
     * @param startDate start date
     * @param endDate end date
     * @return List of SearchHistoryResponse
     */
    public List<SearchHistoryResponse> getSearchHistoryByDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching search history for user: {} between {} and {}", user.getEmail(), startDate, endDate);

        List<SearchHistory> history = searchHistoryRepository.findByUserAndCreatedAtBetween(user, startDate, endDate);

        log.info("Found {} search records in date range", history.size());

        return history.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get trending searches (most searched queries)
     * @param limit the number of results
     * @return List of search queries
     */
    public List<String> getTrendingSearches(int limit) {
        log.info("Fetching trending searches - Limit: {}", limit);

        List<SearchHistory> allSearches = searchHistoryRepository.findAll();

        return allSearches.stream()
                .map(SearchHistory::getQuery)
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Map SearchHistory entity to SearchHistoryResponse DTO
     * @param history the search history entity
     * @return SearchHistoryResponse
     */
    private SearchHistoryResponse mapToResponse(SearchHistory history) {
        return SearchHistoryResponse.builder()
                .id(history.getId())
                .query(history.getQuery())
                .createdAt(history.getCreatedAt().format(DATE_FORMATTER))
                .build();
    }
}

