package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.udacity.movietip.data.db.DataRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private final LiveData<List<Movie>> mAllMovies;

    MovieViewModel(Application application, String category){
        super(application);
        DataRepository mRepository = new DataRepository(application);
        mAllMovies = mRepository.getAllMovies(category);
    }

    public LiveData<List<Movie>> getAllMovies(){
        return mAllMovies;
    }
}
