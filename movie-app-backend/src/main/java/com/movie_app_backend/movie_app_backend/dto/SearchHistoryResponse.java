package com.movie_app_backend.movie_app_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SearchHistoryResponse - DTO for search history
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHistoryResponse {

    private Long id;
    private String query;
    private String createdAt;
}

