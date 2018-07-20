package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.provider.ContactsContract.Data;

import com.udacity.movietip.data.db.DataRepository;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {

    private DataRepository mRepository;
    private LiveData<Movie> mMovie;

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
