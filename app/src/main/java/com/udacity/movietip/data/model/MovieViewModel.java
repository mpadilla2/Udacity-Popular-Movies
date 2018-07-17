package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.udacity.movietip.data.db.DataRepository;
import com.udacity.movietip.data.db.FavoriteMoviesDatabase;

import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends AndroidViewModel{

    private DataRepository mRepository;
    private LiveData<List<Movie>> mAllMovies;
    private LiveData<Movie> mMovie;
    private LiveData<Integer> mMovieCount;

    public MovieViewModel(Application application){
        super(application);
        mRepository = new DataRepository(application);
    }

    public LiveData<List<Movie>> getAllMovies(){
        mAllMovies = mRepository.getAllMovies();
        return mAllMovies;
    }

    public LiveData<List<Movie>> getAllMovies(String type){
        mAllMovies = mRepository.getAllMovies(type);
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