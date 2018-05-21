package com.udacity.movietip.data.remote;

import com.udacity.movietip.data.model.MoviesModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    /* Injected api key with okhttp interceptor
    Reference: https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/ */

    /* Specify endpoint methods to query the TMDB API. */
    @GET("movie/{apiPath}")
    Call<MoviesModel> getJSON(@Path("apiPath") String apiPath);
}
