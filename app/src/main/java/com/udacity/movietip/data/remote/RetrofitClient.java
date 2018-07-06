package com.udacity.movietip.data.remote;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udacity.movietip.BuildConfig;

import java.io.IOException;
import java.text.DateFormat;


import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
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

       ** Interceptor to add API Key
          http://square.github.io/okhttp/
          Reference: https://github.com/square/okhttp/wiki/Interceptors
          Reference: https://mobikul.com/use-interceptor-add-headers-body-retrofit-2-0/
    */


public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitClient(String baseUrl){

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        //HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        //loggingInterceptor.setLevel(Level.BODY);


        // Add logging interceptor to troubleshoot calls. Reference: https://github.com/square/okhttp/wiki/Interceptors
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .addInterceptor(new AuthInterceptor())
                .build();

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    private static class AuthInterceptor implements Interceptor {

        private AuthInterceptor(){
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            HttpUrl url = chain.request().url().newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .build();

            Request request = chain.request().newBuilder()
                    .url(url)
                    .build();

            return chain.proceed(request);
        }
    }
}
