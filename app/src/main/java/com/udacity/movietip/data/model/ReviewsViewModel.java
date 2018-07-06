package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.udacity.movietip.data.db.DataRepository;

import java.util.List;

public class ReviewsViewModel extends AndroidViewModel{

    private DataRepository mRepository;
    private LiveData<List<Reviews>> mAllReviews;

    public ReviewsViewModel(Application application){
        super(application);
        mRepository = new DataRepository(application);
    }

    public LiveData<List<Reviews>> getAllReviews(int movieId){
        mAllReviews = mRepository.getAllReviews(movieId);
        return mAllReviews;
    }

}