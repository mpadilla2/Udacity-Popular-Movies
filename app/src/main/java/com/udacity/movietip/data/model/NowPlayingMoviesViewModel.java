package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.udacity.movietip.data.db.DataRepository;

import java.util.List;

public class NowPlayingMoviesViewModel extends AndroidViewModel{

    private DataRepository mRepository;
    private LiveData<List<Movie>> mAllMovies;
    private LiveData<Movie> mMovie;
    private LiveData<Integer> mMovieCount;
    private static final String MOVIES_NOW_PLAYING = "now_playing";

    public NowPlayingMoviesViewModel(Application application){
        super(application);
        mRepository = new DataRepository(application);
        mAllMovies = mRepository.getAllMovies(MOVIES_NOW_PLAYING);
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