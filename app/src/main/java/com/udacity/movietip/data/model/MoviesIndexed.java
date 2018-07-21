package com.udacity.movietip.data.model;

import java.util.ArrayList;

public class MoviesIndexed {

    /*
    Popular, Top Rated, and Now Playing Movies API queries have identical JSON Schemas;
    So use Generic Movies Object.

    Using GSON: https://github.com/codepath/android_guides/wiki/Leveraging-the-Gson-Library
    If the class fields match the JSON keys, GSON will map automatically.
    OR
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
     */

    private ArrayList<Movie> results;

    /**
     * No args constructor for use in serialization
     */
/*    public MoviesIndexed(){}

    public MoviesIndexed(Integer page, ArrayList<Movie> results, Integer totalResults, Integer totalPages){
        Integer page1 = page;
        this.results = results;
        Integer totalResults1 = totalResults;
        Integer totalPages1 = totalPages;
    }*/


    public ArrayList<Movie> getResults() {
        return results;
    }
}

