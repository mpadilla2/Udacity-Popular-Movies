package com.udacity.movietip.ui.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.movietip.R;
import com.udacity.movietip.data.adapters.MasterGridAdapter;
import com.udacity.movietip.data.adapters.MasterGridAdapter.GridItemClickListener;
import com.udacity.movietip.data.model.Movie;
import com.udacity.movietip.data.model.MoviesIndexed;
import com.udacity.movietip.data.remote.ApiService;
import com.udacity.movietip.data.utils.ApiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterGridFragment extends Fragment {
    private static final String TAG = "MasterGridFragment";

    private Context mContext;
    private MasterGridAdapter mAdapter;
    private ArrayList<Movie> mMovieList;

    /* Create a new instance of MasterGridFragment, providing "category" as an argument.
     * Reference: https://developer.android.com/reference/android/app/Fragment */
    public MasterGridFragment newInstance(String category){
        MasterGridFragment masterGridFragment = new MasterGridFragment();

        // Supply category input as an argument.
        Bundle args = new Bundle();
        args.putString("category", category);
        masterGridFragment.setArguments(args);

        return masterGridFragment;
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // if not, throw an exception
        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate started");

        // Grab the category argument passed in or else default to popular movies category to create new fragment
        String mCategory = getArguments() != null ? getArguments().getString("category") : getString(R.string.movies_popular_path);

        // make the tmdb api call with the appropriate category for the fragment and thus the bottom navigation view in main activity
        if (isActiveNetwork()) loadMovies(mCategory);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView started");

        // Inflates the RecyclerView grid layout of all movie images
        final View rootView = inflater.inflate(R.layout.fragment_master_grid, container, false);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.images_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // Instantiate and set the RecyclerView LayoutManager to a StaggerdGrid
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final List<Movie> movieList = new ArrayList<>();
        // instantiate custom MasterGridAdapter and custom click listener
        // Reference:https://gist.github.com/riyazMuhammad/1c7b1f9fa3065aa5a46f
        mAdapter = new MasterGridAdapter(getActivity(), movieList, new GridItemClickListener() {
            @Override
            public void onGridItemClick(View v, int clickedItemIndex) {
                Log.d(TAG, "clicked position is " + clickedItemIndex);
                //int movieId = movieList.get(clickedItemIndex).getId();
                Movie movie = movieList.get(clickedItemIndex);
                mCallback.onImageSelected(movie);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        // Return the root view
        return rootView;
    }

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnImageClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity name onImageSelected
    public interface OnImageClickListener{
        void onImageSelected(Movie movie);
    }

    public void loadMovies(String category){

        Log.d(TAG, "loadMovies started");

        String language = "en_US";
        int pageNum = 1;

        ApiService mService = ApiUtils.getApiService(getContext());
        mService.getJSON(category, language, pageNum).enqueue(new Callback<MoviesIndexed>() {
            @Override
            public void onResponse(Call<MoviesIndexed> call, Response<MoviesIndexed> response) {
                if (response.body() != null) {
                    List<Movie> movieList = response.body().getResults();
                    mAdapter.setMoviesList(movieList);
                    Log.d(TAG, "data loaded from API");
                }else{
                    Toast.makeText(getActivity(), "Oops! No movies returned!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "null returned from API");
                }
            }

            @Override
            public void onFailure(Call<MoviesIndexed> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.internet_status), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "error loading from API");
            }
        });
    }

    // Reference: https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
    public boolean isActiveNetwork(){

        Log.d(TAG, "isActiveNetwork started");

        ConnectivityManager connectivityManager =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("movies", mMovieList);
        super.onSaveInstanceState(outState);
    }
}
