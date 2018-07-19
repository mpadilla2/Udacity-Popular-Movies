package com.udacity.movietip.data.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.movietip.data.model.Movie;
import com.udacity.movietip.data.model.MoviesIndexed;
import com.udacity.movietip.data.model.Reviews;
import com.udacity.movietip.data.model.ReviewsIndexed;
import com.udacity.movietip.data.model.Trailers;
import com.udacity.movietip.data.model.TrailersIndexed;
import com.udacity.movietip.data.remote.ApiService;
import com.udacity.movietip.data.utils.ApiUtils;
import com.udacity.movietip.data.utils.AppExecutors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// Reference: https://developer.android.com/training/data-storage/room/accessing-data
public class DataRepository {

    private FavoriteMoviesDAO favoriteMoviesDAO;
    private LiveData<List<Movie>> mAllMovies;
    private LiveData<Movie> mMovie;
    private int movieCount;

    ApiService mService;


    public DataRepository(Application application){
        FavoriteMoviesDatabase db = FavoriteMoviesDatabase.getInstance(application);
        favoriteMoviesDAO = db.favoriteMoviesDAO();
        mService = ApiUtils.getApiService();
    }


    // Query is run on a background thread because we're returning LiveData.
    public LiveData<List<Movie>> getAllMovies(){
        mAllMovies = favoriteMoviesDAO.getAllMovies();
        Log.d("DATAREPOSITORY", "LOADED MOVIES FOR FAVORITES FROM DATABASE");
        return mAllMovies;
    }


    // Query is run on a background thread because we're returning LiveData.
    public LiveData<Movie> getMovie(int movieId){
        mMovie = favoriteMoviesDAO.getMovieById(movieId);
        return mMovie;
    }


    public void insert(Movie movie){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                favoriteMoviesDAO.insertMovie(movie);
            }
        });
    }


    public void delete(Movie movie) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                favoriteMoviesDAO.deleteMovie(movie);
            }
        });
    }


    public void toggleFavorite(Movie movie){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                movieCount = favoriteMoviesDAO.getMovieCount(movie.getId());

                if (movieCount > 0){
                    delete(movie);
                } else {
                    insert(movie);
                }
            }
        });
    }


    public LiveData<List<Movie>> getAllMovies(String category){
        String language = "en_US";
        int pageNum = 1;

        final MutableLiveData<List<Movie>> data = new MutableLiveData<>();

        mService.getJSON(category, language, pageNum).enqueue(new Callback<MoviesIndexed>() {
            @Override
            public void onResponse(@NonNull Call<MoviesIndexed> call, @NonNull Response<MoviesIndexed> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    data.setValue(Objects.requireNonNull(response.body()).getResults());
                    Log.d("DATAREPOSITORY", "LOADED MOVIES FOR " + category + " FROM INTERNET API");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesIndexed> call, @NonNull Throwable t) {
                // do something here
                Log.d("DATAREPOSITORY", "LOADING FROM INTERNET API FOR " + category + "FAILED");
            }
        });
        return data;
    }
}
