package com.udacity.movietip.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.movietip.R;
import com.udacity.movietip.data.adapters.MasterGridAdapter;
import com.udacity.movietip.data.adapters.MasterGridAdapter.GridItemClickListener;
import com.udacity.movietip.data.db.FavoriteMoviesDatabase;
import com.udacity.movietip.data.model.Movie;
import com.udacity.movietip.data.model.MovieViewModel;
import com.udacity.movietip.ui.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// todo add paging library https://codelabs.developers.google.com/codelabs/android-paging/index.html#0
public class MasterGridFragment extends Fragment{
    private static final int RECYCLERVIEW_NUM_COLUMNS = 3;
    private static final String MOVIES_POPULAR_PATH = "popular";

    private Context mContext;
    private MasterGridAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Parcelable mGridState;
    private ArrayList<Movie> movieList;
    private FavoriteMoviesDatabase mDb;
    private static final String SAVED_MOVIE_LIST = "Saved Movie List";
    private static final String SAVED_GRID_STATE = "Saved Grid State";
    private MovieViewModel mMovieViewModel;

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

        initAdapter();

        String mCategory = getArguments() != null ? getArguments().getString("category") : MOVIES_POPULAR_PATH;

        // todo schedule job to download the data only if network is available
        // todo once data is downloaded and applied to adapter, refresh the views with the data available

        if (savedInstanceState == null){
            if (!mCategory.equals("favorites")) {
                if (isActiveNetwork()) {
                    loadMovies(mCategory);
                } else {
                    // set view here that shows no internet connection
                    Toast.makeText(getActivity(), "MasterGridFragment: Oops! No network connection!", Toast.LENGTH_LONG).show();
                }
            } else {
                loadMovies(mCategory);
            }
        } else {
            if ( savedInstanceState.containsKey(SAVED_MOVIE_LIST)) {
                Log.d("MasterGridFragment", "movieList restored from saved instance");
                movieList = savedInstanceState.getParcelableArrayList(SAVED_MOVIE_LIST);
                mAdapter.setMoviesList(movieList);
            }
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflates the RecyclerView grid layout of all movie images
        final View rootView = inflater.inflate(R.layout.fragment_master_grid, container, false);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.images_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(mContext, RECYCLERVIEW_NUM_COLUMNS);

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SAVED_GRID_STATE)) {
                mGridState = savedInstanceState.getParcelable(SAVED_GRID_STATE);
                if (mGridState != null) {
                    mLayoutManager.onRestoreInstanceState(mGridState);
                    Log.d("MasterGridFragment", "Grid State supposedly restored");
                }
            }
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.GRID));
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    private OnImageClickListener mCallback;


    // OnImageClickListener interface, calls a method in the host activity name onImageSelected
    public interface OnImageClickListener{
        void onImageSelected(Movie movie);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MasterGridFragment", "onSaveInstanceState");
        mGridState = mLayoutManager.onSaveInstanceState();
        outState.putParcelableArrayList(SAVED_MOVIE_LIST, movieList);
        outState.putParcelable(SAVED_GRID_STATE, mGridState);
    }

    private void initAdapter() {
    /* Instantiate custom MasterGridAdapter and custom click listener
        Reference:https://gist.github.com/riyazMuhammad/1c7b1f9fa3065aa5a46f
    */
        final ArrayList<Movie> movieList = new ArrayList<>();
        mAdapter = new MasterGridAdapter(getActivity(), movieList, new GridItemClickListener() {
            @Override
            public void onGridItemClick(int clickedItemIndex) {
                Movie movie = movieList.get(clickedItemIndex);
                mCallback.onImageSelected(movie);
            }
        });
    }

    private void loadMovies(String category){

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        if (!category.equals("favorites")){
            mMovieViewModel.getAllMovies(category).observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    Log.d("MasterGridFragment", "Updating list of movies from TMDB Api Livedata in ViewModel");
                    movieList = (ArrayList) movies;
                    mAdapter.setMoviesList(movieList);
                }
            });
        } else {
            Log.d("MasterGridFragment", "querying database for favorites");

            mMovieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    Log.d("MasterGridFragment", "Updating list of movies from Database Livedata in ViewModel");
                    movieList = (ArrayList) movies;
                    mAdapter.setMoviesList(movieList);
                }
            });
        }
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
