package com.udacity.movietip.data.remote;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

       /*
    RetrofitClient
       ** Network calls with Retrofit
          Reference: http://square.github.io/retrofit/
          Reference: https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792

       ** Image Loading with Glide
          Reference: https://bumptech.github.io/glide/

       ** TMDB API append_to_response: reduce api calls speed up response
          /movies/popular list updates daily. make a check for this before grabbing the popular list.
          /movies/top_rated

       Similar to Controller class in this tutorial
    */


public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitClient(String baseUrl){

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

}
