package com.udacity.movietip.data.utils;

import android.content.Context;

import com.udacity.movietip.R;
import com.udacity.movietip.data.remote.ApiService;
import com.udacity.movietip.data.remote.RetrofitClient;

public class ApiUtils {
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    /* create the implementation of the ApiService */

    public static ApiService getApiService(Context context) {

        return RetrofitClient
                .getRetrofitClient(BASE_URL)
                .create(ApiService.class);
    }
}
