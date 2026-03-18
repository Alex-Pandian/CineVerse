package com.movie_app_backend.movie_app_backend.client;

import com.movie_app_backend.movie_app_backend.dto.OmdbMovieResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * OmdbApiClient - Client for OMDb API communication
 * Handles all API calls to OMDb and error handling
 */
@Component
public class OmdbApiClient {

    private static final Logger log = LoggerFactory.getLogger(OmdbApiClient.class);
    private final RestTemplate restTemplate;

    @Value("${omdb.api.url}")
    private String omdbApiUrl;

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    public OmdbApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Search movies by name, year, type, and page
     * @param movieName the movie name to search
     * @param year optional year
     * @param type optional type (movie, series, episode)
     * @param page optional page number (default 1)
     * @return OmdbMovieResponse containing search results or error
     */
    public OmdbMovieResponse searchMovies(String movieName, String year, String type, Integer page) {
        try {
            log.info("Searching OMDb for movie: {}", movieName);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(omdbApiUrl)
                    .queryParam("s", movieName)
                    .queryParam("apikey", omdbApiKey);

            if (year != null && !year.isEmpty()) {
                builder.queryParam("y", year);
            }

            if (type != null && !type.isEmpty()) {
                builder.queryParam("type", type);
            }

            if (page != null && page > 1) {
                builder.queryParam("page", page);
            }

            String url = builder.build().toUriString();
            OmdbMovieResponse response = restTemplate.getForObject(url, OmdbMovieResponse.class);

            if (response != null && "False".equalsIgnoreCase(response.getResponse())) {
                log.warn("OMDb API error: {}", response.getError());
                response.setError(response.getError());
            }

            return response;
        } catch (Exception e) {
            log.error("Error searching OMDb API for movie: {}", movieName, e);
            OmdbMovieResponse errorResponse = new OmdbMovieResponse();
            errorResponse.setResponse("False");
            errorResponse.setError("Failed to search movies: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Fetch movie details by IMDb ID
     * @param imdbId the IMDb ID of the movie
     * @return OmdbMovieResponse containing movie details or error
     */
    public OmdbMovieResponse getMovieDetails(String imdbId) {
        try {
            log.info("Fetching OMDb details for IMDb ID: {}", imdbId);

            String url = UriComponentsBuilder.fromUriString(omdbApiUrl)
                    .queryParam("i", imdbId)
                    .queryParam("apikey", omdbApiKey)
                    .build()
                    .toUriString();

            OmdbMovieResponse response = restTemplate.getForObject(url, OmdbMovieResponse.class);

            if (response != null && "False".equalsIgnoreCase(response.getResponse())) {
                log.warn("OMDb API error: {}", response.getError());
            }

            return response;
        } catch (Exception e) {
            log.error("Error fetching OMDb details for IMDb ID: {}", imdbId, e);
            OmdbMovieResponse errorResponse = new OmdbMovieResponse();
            errorResponse.setResponse("False");
            errorResponse.setError("Failed to fetch movie details: " + e.getMessage());
            return errorResponse;
        }
    }
}
