package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class FavoriteMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private Movie mMovie;

    public FavoriteMovieViewModelFactory(Application application, Movie movie){
        mApplication = application;
        mMovie = movie;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new FavoriteMovieViewModel(mApplication, mMovie);
    }
}
