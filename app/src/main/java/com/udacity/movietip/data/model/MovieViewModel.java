package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
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

    public LiveData<Movie> getMovie(Movie movie){
        mMovie = mRepository.getMovie(movie.getId());
        return mMovie;
    }

    public void toggleFavorite(Movie movie){
        mRepository.toggleFavorite(movie);
    }
}