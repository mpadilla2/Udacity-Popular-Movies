package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.udacity.movietip.data.db.DataRepository;

import java.util.List;

public class FavoriteMoviesViewModel extends AndroidViewModel{

    private DataRepository mRepository;
    private LiveData<List<Movie>> mAllMovies;
    private LiveData<Movie> mMovie;
    private LiveData<Integer> mMovieCount;

    public FavoriteMoviesViewModel(Application application){
        super(application);
        mRepository = new DataRepository(application);
        mAllMovies = mRepository.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies(){
        return mAllMovies;
    }

    public LiveData<Movie> getMovie(int movieId){
        mMovie = mRepository.getMovie(movieId);
        return mMovie;
    }

    public void insert(Movie movie) {
        mRepository.insert(movie);
    }

    public void delete(Movie movie) {
        mRepository.delete(movie);
    }
}