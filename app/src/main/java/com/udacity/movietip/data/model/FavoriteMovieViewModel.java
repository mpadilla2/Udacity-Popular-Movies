package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.udacity.movietip.data.db.DataRepository;

public class FavoriteMovieViewModel extends AndroidViewModel {

    private final DataRepository mRepository;
    private final LiveData<Movie> mMovie;

    FavoriteMovieViewModel(Application application, Movie movie){
        super(application);
        mRepository = new DataRepository(application);
        mMovie = mRepository.getMovie(movie.getId());
    }

    public LiveData<Movie> getFavoriteMovie() {
        return mMovie;
    }

    public void toggleFavorite(Movie movie){
        mRepository.toggleFavorite(movie);
    }
}
