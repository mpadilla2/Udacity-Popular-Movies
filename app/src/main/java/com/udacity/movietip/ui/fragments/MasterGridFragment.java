package com.udacity.movietip.ui.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterGridFragment extends Fragment{
    private static final int RECYCLERVIEW_NUM_COLUMNS = 3;
    private static final String MOVIES_POPULAR_PATH = "popular";

    private Context mContext;
    private MasterGridAdapter mAdapter;

    /* Create a new instance of MasterGridFragment, providing "category" as an argument.
     * Reference: https://developer.android.com/reference/android/app/Fragment
     */
    public MasterGridFragment newInstance(String category){
        MasterGridFragment masterGridFragment = new MasterGridFragment();

        mContext = getContext();

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

        // Grab the category argument used to create new fragment or else default to popular movies category
        String mCategory = getArguments() != null ? getArguments().getString("category") : MOVIES_POPULAR_PATH;

        // If the network is available, make the tmdb api call with the appropriate category for the fragment
        if (isActiveNetwork()){
            loadMovies(mCategory);
        } else {
            Toast.makeText(getActivity(), "Oops! No network connection!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflates the RecyclerView grid layout of all movie images
        final View rootView = inflater.inflate(R.layout.fragment_master_grid, container, false);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.images_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // Instantiate and set the RecyclerView LayoutManager to a grid with 3 columns
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, RECYCLERVIEW_NUM_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /* Instantiate custom MasterGridAdapter and custom click listener
           Reference:https://gist.github.com/riyazMuhammad/1c7b1f9fa3065aa5a46f
         */
        final List<Movie> movieList = new ArrayList<>();
        mAdapter = new MasterGridAdapter(getActivity(), movieList, new GridItemClickListener() {
            @Override
            public void onGridItemClick(int clickedItemIndex) {
                Movie movie = movieList.get(clickedItemIndex);
                mCallback.onImageSelected(movie);
            }
        });


        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    private OnImageClickListener mCallback;


    // OnImageClickListener interface, calls a method in the host activity name onImageSelected
    public interface OnImageClickListener{
        void onImageSelected(Movie movie);
    }


    private void loadMovies(String category){

        String language = "en_US";
        int pageNum = 1;

        ApiService mService = ApiUtils.getApiService();
        mService.getJSON(category, language, pageNum).enqueue(new Callback<MoviesIndexed>() {
            @Override
            public void onResponse(@NonNull Call<MoviesIndexed> call, @NonNull Response<MoviesIndexed> response) {
                assert response.body() != null;
                List<Movie> movieList = response.body() != null ? Objects.requireNonNull(response.body()).getResults() : null;
                mAdapter.setMoviesList(movieList);
            }

            @Override
            public void onFailure(@NonNull Call<MoviesIndexed> call, @NonNull Throwable t) {
                Toast.makeText(mContext, getString(R.string.internet_status), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
       Reference: https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
     */
    private boolean isActiveNetwork(){

        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext())
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


}
