package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.udacity.movietip.data.db.DataRepository;

import java.util.List;

public class TrailersViewModel extends AndroidViewModel{

    private DataRepository mRepository;
    private LiveData<List<Trailers>> mAllTrailers;

    public TrailersViewModel(Application application){
        super(application);
        mRepository = new DataRepository(application);
    }

    public LiveData<List<Trailers>> getAllTrailers(Integer movieId){
        mAllTrailers = mRepository.getAllTrailers(movieId);
        return mAllTrailers;
    }

}