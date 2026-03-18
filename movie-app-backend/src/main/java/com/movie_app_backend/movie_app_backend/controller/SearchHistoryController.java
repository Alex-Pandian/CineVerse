package com.movie_app_backend.movie_app_backend.controller;

import com.movie_app_backend.movie_app_backend.dto.SearchHistoryResponse;
import com.movie_app_backend.movie_app_backend.model.User;
import com.movie_app_backend.movie_app_backend.repository.UserRepository;
import com.movie_app_backend.movie_app_backend.service.SearchHistoryService;
import com.movie_app_backend.movie_app_backend.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SearchHistoryController - REST controller for user search history
 * Handles storing and retrieving search queries
 * All endpoints require authentication
 */
@RestController
@RequestMapping("/api/history")
public class SearchHistoryController {

    private static final Logger log = LoggerFactory.getLogger(SearchHistoryController.class);
    private final SearchHistoryService searchHistoryService;
    private final UserRepository userRepository;

    public SearchHistoryController(SearchHistoryService searchHistoryService, UserRepository userRepository) {
        this.searchHistoryService = searchHistoryService;
        this.userRepository = userRepository;
    }

    /**
     * Record a search query
     * @param query the search query
     * @return SearchHistoryResponse
     */
    @PostMapping
    public ResponseEntity<SearchHistoryResponse> recordSearch(@RequestParam String query) {
        log.info("Recording search query: {}", query);

        User user = SecurityUtil.getCurrentUser(userRepository);
        SearchHistoryResponse response = searchHistoryService.recordSearch(user, query);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get user's search history
     * @param limit the number of results (default 50)
     * @return List of SearchHistoryResponse
     */
    @GetMapping
    public ResponseEntity<List<SearchHistoryResponse>> getSearchHistory(
            @RequestParam(required = false, defaultValue = "50") int limit) {

        log.info("Fetching search history - Limit: {}", limit);

        User user = SecurityUtil.getCurrentUser(userRepository);
        List<SearchHistoryResponse> history = searchHistoryService.getUserSearchHistory(user, limit);

        return ResponseEntity.ok(history);
    }
}
