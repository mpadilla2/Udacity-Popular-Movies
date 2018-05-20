package com.udacity.movietip.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.udacity.movietip.R;
import com.udacity.movietip.data.model.MoviesModel;
import com.udacity.movietip.data.remote.ApiService;
import com.udacity.movietip.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ApiService mService;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.test_json_return);
        mService = ApiUtils.getApiService(this);

        loadMoviesData();
    }

    public void loadMoviesData(){
        // abstract the method further so it's getMoviesObject(String objectType)
        mService.getPopularMovies().enqueue(new Callback<MoviesModel>() {
            @Override
            public void onResponse(Call<MoviesModel> call, Response<MoviesModel> response) {
                mTextView.setText(response.message());
                Log.d("MainActivity", "data loaded from API");
            }

            @Override
            public void onFailure(Call<MoviesModel> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
            }
        });
    }
}
