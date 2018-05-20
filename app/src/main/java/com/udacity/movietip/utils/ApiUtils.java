package com.udacity.movietip.utils;

import com.udacity.movietip.BuildConfig;
import com.udacity.movietip.data.model.MoviesModel;
import com.udacity.movietip.data.remote.RetrofitClient;
import com.udacity.movietip.data.remote.TMDBApiService;

import retrofit2.Call;

public class ApiUtils {

    /* create the implementation of the TMDBApiService
       Pass in the movie object type desired; check which type and make appropriate interface call
     */

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static TMDBApiService getTMDBApiService() {
        return RetrofitClient.getRetrofitClient(BASE_URL).create(TMDBApiService.class);
    }
}
