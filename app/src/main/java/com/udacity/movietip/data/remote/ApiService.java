package com.udacity.movietip.data.remote;

import com.udacity.movietip.data.model.MoviesIndexed;
import com.udacity.movietip.data.model.ReviewsIndexed;
import com.udacity.movietip.data.model.TrailersIndexed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /* Injected api key with okhttp interceptor
    Reference: https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/ */

    /* Specify endpoint methods to query the TMDB API. */
    @GET("movie/{apiPath}")
    Call<MoviesIndexed> getJSON(@Path("apiPath") String apiPath,
                                @Query("language") String language,
                                @Query("page") int pageNum);

    @GET("movie/{movie_id}/videos")
    Call<TrailersIndexed> getTrailers(@Query("movieId") Integer movieId);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsIndexed> getReviews(@Query("movieId") Integer movieId);
}
