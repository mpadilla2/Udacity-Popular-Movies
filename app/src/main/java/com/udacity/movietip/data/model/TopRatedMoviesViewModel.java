package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.udacity.movietip.data.db.DataRepository;

import java.util.List;

public class TopRatedMoviesViewModel extends AndroidViewModel{

    private DataRepository mRepository;
    private LiveData<List<Movie>> mAllMovies;
    private LiveData<Movie> mMovie;
    private LiveData<Integer> mMovieCount;
    private static final String MOVIES_TOP_RATED = "top_rated";

    public TopRatedMoviesViewModel(Application application){
        super(application);
        mRepository = new DataRepository(application);
        mAllMovies = mRepository.getAllMovies(MOVIES_TOP_RATED);
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