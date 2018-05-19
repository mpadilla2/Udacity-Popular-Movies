package com.udacity.movietip.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udacity.movietip.models.MoviesModel;

import java.text.DateFormat;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDBApiControllerNetwork {

       /*
    TMDBApiControllerNetwork
       ** Network calls with Retrofit
          Reference: http://square.github.io/retrofit/

       ** Image Loading with Glide
          Reference: https://bumptech.github.io/glide/

       ** TMDB API append_to_response: reduce api calls speed up response
          /movies/popular list updates daily. make a check for this before grabbing the popular list.
          /movies/top_rated

       http://www.vogella.com/tutorials/Retrofit/article.html

       Similar to Controller class in this tutorial
    */

    /*
    In here I should create the Retrofit client, call the TMBD API and handle the result
     */

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static final String API_KEY = "";

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitClient(){

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        if (retrofit == null){
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }
}
