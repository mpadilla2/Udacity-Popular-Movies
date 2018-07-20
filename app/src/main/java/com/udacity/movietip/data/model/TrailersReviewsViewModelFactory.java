package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class TrailersReviewsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private int mMovieId;

    public TrailersReviewsViewModelFactory(Application application, int id){
        mApplication = application;
        mMovieId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new TrailersReviewsViewModel(mApplication, mMovieId);
    }
}
