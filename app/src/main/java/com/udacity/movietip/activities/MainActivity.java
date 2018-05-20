package com.udacity.movietip.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.udacity.movietip.BuildConfig;
import com.udacity.movietip.R;
import com.udacity.movietip.data.model.MoviesModel;
import com.udacity.movietip.data.remote.TMDBApiService;
import com.udacity.movietip.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TMDBApiService mService;
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.test_json_return);
        mService = ApiUtils.getTMDBApiService();

        loadMoviesData();
    }

    public void loadMoviesData(){
        mService.getPopularMovies(API_KEY).enqueue(new Callback<MoviesModel>() {
            @Override
            public void onResponse(Call<MoviesModel> call, Response<MoviesModel> response) {
                mTextView.setText(response.body().getTotalResults());
            }

            @Override
            public void onFailure(Call<MoviesModel> call, Throwable t) {

            }
        });
    }
}
