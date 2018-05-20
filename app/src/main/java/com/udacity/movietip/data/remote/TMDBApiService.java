package com.udacity.movietip.data.remote;

import com.udacity.movietip.data.model.MoviesModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBApiService {

    /* Reference: https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/ */

    /* Specify endpoint methods to query the TMDB API. */
    @GET("movie/top_rated")
    Call<MoviesModel> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesModel> getPopularMovies(@Query("api_key") String apiKey);
}
