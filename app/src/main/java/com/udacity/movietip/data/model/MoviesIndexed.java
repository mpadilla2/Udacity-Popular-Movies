package com.udacity.movietip.data.model;

import java.util.List;

public class MoviesIndexed {

    /*
    Both Popular and Top Rated Movies API queries have identical JSON Schemas;
    So abstract to Generic Movies Object.

    Using GSON: https://github.com/codepath/android_guides/wiki/Leveraging-the-Gson-Library
    If the class fields match the JSON keys, GSON will map automatically.
    OR
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

    page Integer
    results List<Movie>
    totalResults Integer
    totalPages Integer


     */

    private Integer page;
    private List<Movie> results;
    private Integer totalResults;
    private Integer totalPages;

    /**
     * No args constructor for use in serialization
     */
    public MoviesIndexed(){}

    public MoviesIndexed(Integer page, List<Movie> results, Integer totalResults, Integer totalPages){
        this.page = page;
        this.results = results;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
    }

    public void setPage(Integer page){
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

    public MoviesIndexed withPage(Integer page){
        this.page = page;
        return this;
    }

    public void setResults(List<Movie> results){
        this.results = results;
    }

    public List<Movie> getResults() {
        return results;
    }

    public MoviesIndexed withResults(List<Movie> results){
        this.results = results;
        return this;
    }

    public void setTotalResults(Integer totalResults){
        this.totalResults = totalResults;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public MoviesIndexed withTotalResults(Integer totalResults){
        this.totalResults = totalResults;
        return this;
    }

    public void setTotalPages(Integer totalPages){
        this.totalPages = totalPages;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public MoviesIndexed withTotalPages(Integer totalPages){
        this.totalPages = totalPages;
        return this;
    }
}

