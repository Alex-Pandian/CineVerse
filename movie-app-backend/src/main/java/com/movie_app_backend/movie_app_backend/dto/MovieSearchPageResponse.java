package com.movie_app_backend.movie_app_backend.dto;

import java.util.List;

/**
 * MovieSearchPageResponse - paginated search response
 */
public class MovieSearchPageResponse {

    private List<MovieSearchResponse> results;
    private int totalResults;
    private int totalPages;
    private int page;
    private String errorMessage;

    public MovieSearchPageResponse() {}

    public MovieSearchPageResponse(List<MovieSearchResponse> results, int totalResults, int totalPages, int page, String errorMessage) {
        this.results = results;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.page = page;
        this.errorMessage = errorMessage;
    }

    public List<MovieSearchResponse> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPage() {
        return page;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setResults(List<MovieSearchResponse> results) {
        this.results = results;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
