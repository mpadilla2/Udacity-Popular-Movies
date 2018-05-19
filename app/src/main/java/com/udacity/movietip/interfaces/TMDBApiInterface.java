package com.udacity.movietip.interfaces;

import com.udacity.movietip.models.MoviesModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class TMDBApiInterface {

        /* http://www.vogella.com/tutorials/Retrofit/article.html
    Similar to GerritAPI interface in this tutorial

    Specify endpoint methods to query the TMDB API.
     */
    public interface GetAPI {
            @GET("movie/top_rated")
            Call<MoviesModel> getTopRatedMovies(@Query("api_key") String apiKey);

            @GET("movie/popular")
            Call<MoviesModel> getPopularMovies(@Query("api_key") String apiKey);
        }
}
