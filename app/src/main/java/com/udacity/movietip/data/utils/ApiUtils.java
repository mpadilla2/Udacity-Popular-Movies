package com.udacity.movietip.data.utils;

import com.udacity.movietip.data.remote.ApiService;
import com.udacity.movietip.data.remote.RetrofitClient;

public class ApiUtils {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    /* create the implementation of the ApiService */

    public static ApiService getApiService() {

        return RetrofitClient
                .getRetrofitClient(BASE_URL)
                .create(ApiService.class);
    }
}
