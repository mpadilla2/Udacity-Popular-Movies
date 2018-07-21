package com.udacity.movietip.data.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.udacity.movietip.data.db.DataRepository;

import java.util.List;

public class TrailersReviewsViewModel extends AndroidViewModel {

    private final LiveData<List<Trailers>> mTrailers;
    private final LiveData<List<Reviews>> mReviews;

    TrailersReviewsViewModel(Application application, int id){
        super(application);
        DataRepository mRepository = new DataRepository(application);
        mTrailers = mRepository.getAllTrailers(id);
        mReviews = mRepository.getAllReviews(id);
    }

    public LiveData<List<Trailers>> getAllTrailers() {
        return mTrailers;
    }

    public LiveData<List<Reviews>> getAllReviews(){
        return mReviews;
    }
}
