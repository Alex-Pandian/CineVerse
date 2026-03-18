package com.movie_app_backend.movie_app_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * FavoriteResponse - DTO for favorite movies
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponse {

    private Long id;
    private String imdbId;
    private String title;
    private String poster;
    private String createdAt;
}

