package com.udacity.movietip.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.udacity.movietip.R;
import com.udacity.movietip.data.adapters.MovieGridAdapter;
import com.udacity.movietip.data.adapters.MovieGridAdapter.GridItemClickListener;
import com.udacity.movietip.data.model.FavoriteMoviesViewModel;
import com.udacity.movietip.data.model.Movie;
import com.udacity.movietip.data.model.NowPlayingMoviesViewModel;
import com.udacity.movietip.data.model.PopularMoviesViewModel;
import com.udacity.movietip.data.model.TopRatedMoviesViewModel;
import com.udacity.movietip.ui.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Todo https://google-developer-training.gitbooks.io/android-developer-advanced-course-concepts/unit-1-expand-the-user-experience/lesson-1-fragments/1-2-c-fragment-lifecycle-and-communications/1-2-c-fragment-lifecycle-and-communications.html
public class MovieGridFragment extends ViewLifecycleFragment{

    private static final int RECYCLERVIEW_NUM_COLUMNS = 3;
    private static final String MOVIES_POPULAR = "popular";
    private static final String MOVIES_TOP_RATED = "top_rated";
    private static final String MOVIES_NOW_PLAYING = "now_playing";
    private static final String MOVIES_FAVORITES = "favorites";

    private Context mContext;
    private MovieGridAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Parcelable mGridState;
    private static final String SAVED_MOVIE_LIST = "Saved Movie List";
    private static final String SAVED_GRID_STATE = "Saved Grid State";
    private static final String SAVED_FRAGMENT_CATEGORY = "Fragment Category";
    private String mCategory;
    private RecyclerView mRecyclerView;
    private PopularMoviesViewModel mPopularMoviesViewModel;
    private TopRatedMoviesViewModel mTopRatedMoviesViewModel;
    private NowPlayingMoviesViewModel mNowPlayingMoviesViewModel;
    private FavoriteMoviesViewModel mFavoritesViewModel;


    /* Create a new instance of MovieGridFragment, providing "category" as an argument.
     * Reference: https://developer.android.com/reference/android/app/Fragment
     */
    public MovieGridFragment newInstance(String category){
        MovieGridFragment movieGridFragment = new MovieGridFragment();

        mContext = getContext();

        // Supply category input as an argument.
        Bundle args = new Bundle();
        args.putString("category", category);
        movieGridFragment.setArguments(args);

        return movieGridFragment;
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
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflates the RecyclerView grid layout of all movie images
        final View rootView = inflater.inflate(R.layout.fragment_master_grid, container, false);
        mRecyclerView = rootView.findViewById(R.id.images_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(mContext, RECYCLERVIEW_NUM_COLUMNS);

        mRecyclerView.setLayoutManager(mLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true, 0));
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // savedInstanceState check for previous category instance
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SAVED_FRAGMENT_CATEGORY)){
                mCategory = savedInstanceState.getString(SAVED_FRAGMENT_CATEGORY);
                Log.d("MOVIEGRIDFRAGMENT", "A SAVED INSTANCE OF CATEGORY EXISTS AS " + mCategory);
            }
        } else {
            // getArguments category for initial Fragment creation
            mCategory = getArguments() != null ? getArguments().getString("category") : MOVIES_POPULAR;
            Log.d("MOVIEGRIDFRAGMENT", "A SAVED INSTANCE OF CATEGORY DOESN'T EXIST! CREATING NEW " + mCategory);
        }

        switch (mCategory){
            case MOVIES_POPULAR:
                mPopularMoviesViewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel.class);
                break;
            case MOVIES_TOP_RATED:
                mTopRatedMoviesViewModel = ViewModelProviders.of(this).get(TopRatedMoviesViewModel.class);
                break;
            case MOVIES_NOW_PLAYING:
                mNowPlayingMoviesViewModel = ViewModelProviders.of(this).get(NowPlayingMoviesViewModel.class);
                break;
            case MOVIES_FAVORITES:
                mFavoritesViewModel = ViewModelProviders.of(this).get(FavoriteMoviesViewModel.class);
                break;
        }

        loadMovies(mCategory);
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
        outState.putString(SAVED_FRAGMENT_CATEGORY, mCategory);
    }


    private void initAdapter() {
    /* Instantiate custom MovieGridAdapter and custom click listener
        Reference:https://gist.github.com/riyazMuhammad/1c7b1f9fa3065aa5a46f
    */
        final List<Movie> movieList = new ArrayList<>();
        mAdapter = new MovieGridAdapter(getActivity(), movieList, new GridItemClickListener() {
            @Override
            public void onGridItemClick(int clickedItemIndex) {
                Movie movie = movieList.get(clickedItemIndex);
                mCallback.onImageSelected(movie);
            }
        });
    }


    private void loadMovies(String category){

        final Observer<List<Movie>> movieListObserver = new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mAdapter.setMoviesList(movies);
            }
        };


        switch (mCategory){
            case MOVIES_POPULAR:
                mPopularMoviesViewModel.getAllMovies().observe(Objects.requireNonNull(getViewLifecycleOwner()), movieListObserver);
                break;
            case MOVIES_TOP_RATED:
                mTopRatedMoviesViewModel.getAllMovies().observe(Objects.requireNonNull(getViewLifecycleOwner()), movieListObserver);
                break;
            case MOVIES_NOW_PLAYING:
                mNowPlayingMoviesViewModel.getAllMovies().observe(Objects.requireNonNull(getViewLifecycleOwner()), movieListObserver);
                break;
            case MOVIES_FAVORITES:
                mFavoritesViewModel.getAllMovies().observe(Objects.requireNonNull(getViewLifecycleOwner()), movieListObserver);
                break;
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


    /**
     * Converting dp to pixel
     * Reference: https://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
     */
    private int dpToPx(int dp) {
        //Resources r = getResources();
        //return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        return (int)(dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
