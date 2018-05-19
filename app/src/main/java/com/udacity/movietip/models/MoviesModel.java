package com.udacity.movietip.models;

import java.util.List;

public class MoviesModel {

    /*
    Both Popular and Top Rated Movies API queries have identical JSON Schemas;
    So abstract to Generic Movies Object.

    Using GSON: https://github.com/codepath/android_guides/wiki/Leveraging-the-Gson-Library
    If the class fields match the JSON keys, GSON will map automatically.
    OR
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

    page Integer
    results List<MoviesResultsModel>
    totalResults Integer
    totalPages Integer


     */

    private Integer page;
    private List<MoviesResultsModel> results;
    private Integer totalResults;
    private Integer totalPages;

    /**
     * No args constructor for use in serialization
     */
    public MoviesModel(){}

    public MoviesModel(Integer page, List<MoviesResultsModel> results, Integer totalResults, Integer totalPages){
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

    public MoviesModel withPage(Integer page){
        this.page = page;
        return this;
    }

    public void setResults(List<MoviesResultsModel> results){
        this.results = results;
    }

    public List<MoviesResultsModel> getResults() {
        return results;
    }

    public MoviesModel withResults(List<MoviesResultsModel> results){
        this.results = results;
        return this;
    }

    public void setTotalResults(Integer totalResults){
        this.totalResults = totalResults;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public MoviesModel withTotalResults(Integer totalResults){
        this.totalResults = totalResults;
        return this;
    }

    public void setTotalPages(Integer totalPages){
        this.totalPages = totalPages;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public MoviesModel withTotalPages(Integer totalPages){
        this.totalPages = totalPages;
        return this;
    }
}

