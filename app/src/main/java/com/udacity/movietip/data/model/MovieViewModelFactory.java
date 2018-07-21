package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application mApplication;
    private final String mCategory;

    public MovieViewModelFactory(Application application, String category){
        mApplication = application;
        mCategory = category;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieViewModel(mApplication, mCategory);
    }
}
