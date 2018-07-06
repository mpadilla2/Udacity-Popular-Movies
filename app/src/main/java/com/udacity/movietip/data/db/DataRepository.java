package com.udacity.movietip.data.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.udacity.movietip.R;
import com.udacity.movietip.data.model.Movie;
import com.udacity.movietip.data.model.MovieViewModel;
import com.udacity.movietip.data.model.MoviesIndexed;
import com.udacity.movietip.data.model.Reviews;
import com.udacity.movietip.data.model.ReviewsIndexed;
import com.udacity.movietip.data.model.Trailers;
import com.udacity.movietip.data.model.TrailersIndexed;
import com.udacity.movietip.data.remote.ApiService;
import com.udacity.movietip.data.utils.ApiUtils;
import com.udacity.movietip.data.utils.AppExecutors;

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
    ApiService mService;


    public DataRepository(Application application){
        FavoriteMoviesDatabase db = FavoriteMoviesDatabase.getInstance(application);
        favoriteMoviesDAO = db.favoriteMoviesDAO();
        mService = ApiUtils.getApiService();
    }


    // Query is run on a background thread because we're returning LiveData.
    public LiveData<List<Movie>> getAllMovies(){
        mAllMovies = favoriteMoviesDAO.getAllMovies();
        return mAllMovies;
    }


    // Query is run on a background thread because we're returning LiveData.
    public LiveData<Movie> getMovie(Integer movieId){
        mMovie = favoriteMoviesDAO.getMovieById(movieId);
        return mMovie;
    }


    public void toggleFavorite(Movie movie) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Integer movieCount = favoriteMoviesDAO.getMovieCount(movie.getId());

                if (movieCount == 0 ){
                    favoriteMoviesDAO.insertMovie(movie);
                } else {
                    favoriteMoviesDAO.deleteMovie(movie);
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
                    Log.d("DataRepository", "loaded movies from internet api");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesIndexed> call, @NonNull Throwable t) {
                // do something here
                Log.d("DataRepository", "Loading from internet api failed");
            }
        });
        return data;
    }


    public LiveData<List<Trailers>> getAllTrailers(Integer movieId) {

        final MutableLiveData<List<Trailers>> data = new MutableLiveData<>();

        mService.getTrailers(movieId).enqueue(new Callback<TrailersIndexed>() {
            @Override
            public void onResponse(@NonNull Call<TrailersIndexed> call, @NonNull Response<TrailersIndexed> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    data.setValue(Objects.requireNonNull(response.body()).getResults());
                    Log.d("DataRepository", "loaded trailers from internet api");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailersIndexed> call, @NonNull Throwable t) {
                Log.d("DataRepository", "Loading from internet api failed");
            }
        });
        return data;
    }


    public LiveData<List<Reviews>> getAllReviews(Integer movieId) {

        final MutableLiveData<List<Reviews>> data = new MutableLiveData<>();

        mService.getReviews(movieId).enqueue(new Callback<ReviewsIndexed>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsIndexed> call, @NonNull Response<ReviewsIndexed> response) {
                assert response.body() != null;
                data.setValue(Objects.requireNonNull(response.body()).getResults());
                Log.d("DataRepository", "Loaded reviews from internet api");
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsIndexed> call, @NonNull Throwable t) {
                Log.d("DataRepository", "Loading from internet api failed");
            }
        });
        return data;
    }
}
