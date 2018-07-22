package com.udacity.movietip.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.udacity.movietip.R;
import com.udacity.movietip.data.adapters.ReviewsAdapter;
import com.udacity.movietip.data.adapters.TrailersAdapter;

import com.udacity.movietip.data.model.FavoriteMovieViewModel;
import com.udacity.movietip.data.model.FavoriteMovieViewModelFactory;
import com.udacity.movietip.data.model.Movie;
import com.udacity.movietip.data.model.Reviews;
import com.udacity.movietip.data.model.Trailers;
import com.udacity.movietip.data.model.TrailersReviewsViewModel;
import com.udacity.movietip.data.model.TrailersReviewsViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity
        extends AppCompatActivity{

    private static final String EXTRA_MOVIE_ITEM = "Movie Item";
    private static final String SAVED_INSTANCE_MOVIE_ITEM = "Saved Movie";
    private static final int IMAGE_LOADING = R.drawable.ic_image_loading;
    private static final int BROKEN_IMAGE = R.drawable.ic_broken_image;

    private final Context mContext;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private ImageView mPosterImageView;
    private ImageView mBackdropImageView;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private TextView mReleaseDateTextView;
    private TextView mOverViewTextView;
    private TextView mVoteCountTextview;
    private TextView mVoteAvgTextview;
    private android.support.v7.widget.Toolbar mToolbar;
    private ImageButton mFavoritesButton;
    private RecyclerView mTrailersRecyclerView;
    private RecyclerView mReviewsRecyclerView;

    private Movie movie;
    private FavoriteMovieViewModel favoriteMovieViewModel;

    public DetailActivity() {
        mContext = this;
    }

    /*
      Parcelable concepts applied from: https://www.youtube.com/watch?v=WBbsvqSu0is
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        initViews();
        initRecyclerViews();

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SAVED_INSTANCE_MOVIE_ITEM)) {
                movie = savedInstanceState.getParcelable(SAVED_INSTANCE_MOVIE_ITEM);
            }
        } else {
            Intent intent = getIntent();

            if (intent != null && intent.hasExtra(EXTRA_MOVIE_ITEM)){
                movie = intent.getParcelableExtra(EXTRA_MOVIE_ITEM);
            }
        }

        setUpFavoritesViewModel();
        setUpFavoritesButton();

        if (isActiveNetwork()){
            loadTrailersAndReviews();
        } else {
            Toast.makeText(this, "Oops! No network connection!", Toast.LENGTH_LONG).show();
        }

        populateUI(movie);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_INSTANCE_MOVIE_ITEM, movie);
        super.onSaveInstanceState(outState);
    }


    private void loadTrailersAndReviews() {

        TrailersReviewsViewModelFactory trailersReviewsViewModelFactory =
                new TrailersReviewsViewModelFactory(getApplication(), movie.getId());

        TrailersReviewsViewModel mTrailersReviewsViewModel = ViewModelProviders
                .of(this, trailersReviewsViewModelFactory).get(TrailersReviewsViewModel.class);

        final Observer<List<Trailers>> trailersListObserver = trailersList -> {
            if (trailersList != null) {
                mTrailersAdapter.setTrailersList(trailersList);
                mTrailersRecyclerView.setAdapter(mTrailersAdapter);
            } else {
                mTrailersRecyclerView.setVisibility(View.GONE);
            }
        };

        final Observer<List<Reviews>> reviewsListObserver = reviews -> {
            if (reviews != null){
                mReviewsAdapter.setReviewsList(reviews);
                mReviewsRecyclerView.setAdapter(mReviewsAdapter);
            } else {
                mReviewsRecyclerView.setVisibility(View.GONE);
            }            };

        mTrailersReviewsViewModel.getAllTrailers().observe(this, trailersListObserver);
        mTrailersReviewsViewModel.getAllReviews().observe(this, reviewsListObserver);

    }


    private void setUpFavoritesButton() {
        mFavoritesButton.setOnClickListener(v -> favoriteMovieViewModel.toggleFavorite(movie));
    }


    private void setUpFavoritesViewModel(){

        FavoriteMovieViewModelFactory favoriteMovieViewModelFactory =
                new FavoriteMovieViewModelFactory(getApplication(),movie);
        favoriteMovieViewModel =
                ViewModelProviders.of(this, favoriteMovieViewModelFactory).get(FavoriteMovieViewModel.class);

        final Observer<Movie> favoriteMovieObserver = movie -> {
            if (movie != null){
                mFavoritesButton.setImageResource(R.drawable.ic_favorite_red_24dp);
            } else {
                mFavoritesButton.setImageResource(R.drawable.ic_favorite_border_red_24dp);
            }
        };
        favoriteMovieViewModel.getFavoriteMovie().observe(this, favoriteMovieObserver);
    }


    private void initRecyclerViews() {

        RecyclerView.LayoutManager mTrailersLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(mTrailersLayoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mReviewsLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);

        final List<Trailers> trailersList = new ArrayList<>();
        // Reference: https://www.codeproject.com/Tips/1229751/Handle-Click-Events-of-Multiple-Buttons-Inside-a
        mTrailersAdapter = new TrailersAdapter(this, trailersList, new TrailersAdapter.ItemClickListener() {
            @Override
            public void trailerOnClick(int clickedItemIndex) {
                // grab the clicked trailer
                Trailers trailer = trailersList.get(clickedItemIndex);

                // Reference: https://developer.android.com/guide/components/intents-common#Music
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                trailerIntent.setData(Uri.parse(trailer.getTrailerUrl()));

                // Reference: https://developer.android.com/training/basics/intents/sending#AppChooser
                Intent chooser = Intent.createChooser(trailerIntent, "Choose Player");

                if (trailerIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }

            @Override
            public void shareOnClick(int clickedItemIndex) {
                // create share intent
                Trailers trailer = trailersList.get(clickedItemIndex);

                // Reference: https://developer.android.com/training/sharing/
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, trailer.getTrailerUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Intent chooser = Intent.createChooser(intent, "Share video to...");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }
        });

        final List<Reviews> reviewsList = new ArrayList<>();
        mReviewsAdapter = new ReviewsAdapter(this, reviewsList, clickedItemIndex -> {
            // grab the clicked review
            Reviews review = reviewsList.get(clickedItemIndex);

            Intent reviewIntent = new Intent(Intent.ACTION_VIEW);
            reviewIntent.setData(Uri.parse(review.getUrl()));

            Intent chooser = Intent.createChooser(reviewIntent, "Choose Browser");

            if (reviewIntent.resolveActivity(getPackageManager()) != null){
                startActivity(chooser);
            }
        });
    }


    private void initViews() {

        mPosterImageView = findViewById(R.id.movie_poster_imageView);
        mBackdropImageView = findViewById(R.id.detail_movie_backdrop_imageView);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        mReleaseDateTextView = findViewById(R.id.detail_movie_release_date);
        mOverViewTextView = findViewById(R.id.detail_movie_overview);
        mVoteCountTextview = findViewById(R.id.detail_movie_vote_count_textView);
        mVoteAvgTextview = findViewById(R.id.movie_detail_vote_average);
        mToolbar = findViewById(R.id.toolbar);
        mTrailersRecyclerView = findViewById(R.id.detail_movie_trailers_recyclerview);
        mReviewsRecyclerView = findViewById(R.id.detail_movie_reviews_recyclerview);
        mFavoritesButton = findViewById(R.id.detail_movie_favorites_button);

    }


    private void populateUI(Movie movie){

        if (movie == null) {
            return;
        }

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        mCollapsingToolbar.setTitle(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mOverViewTextView.setText(movie.getOverview());
        mVoteCountTextview.setText(String.valueOf(movie.getVoteCount()));
        mVoteAvgTextview.setText(String.valueOf(movie.getVoteAverage()));

        // Reference: https://developer.android.com/guide/topics/resources/runtime-changes
        if(isLandscape) {
            setConstraintBackground(movie.getBackdropUrl());
        } else {
            setConstraintBackground(movie.getPosterUrl());
        }

        loadImageIntoView(this, movie.getPosterUrl(), mPosterImageView);
        loadImageIntoView(this, movie.getBackdropUrl(), mBackdropImageView);
    }


    private void setConstraintBackground(String imageUrl) {

        final ConstraintLayout scrollingConstraint = findViewById(R.id.detail_scrolling_constraint);

        // Reference: https://github.com/bumptech/glide/wiki
        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .fitCenter()
                        .placeholder(IMAGE_LOADING)
                        .error(BROKEN_IMAGE))
                .into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
                // https://developer.android.com/reference/android/graphics/drawable/Drawable.html#setAlpha(int)
                resource.setAlpha(50);
                scrollingConstraint.setBackground(resource);
            }
        });
    }


    private void loadImageIntoView(Context context, String imageUrl, ImageView imageView){

        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .optionalCenterCrop()
                        .placeholder(IMAGE_LOADING)
                        .error(BROKEN_IMAGE))
                .into(imageView);
    }


    /*
    Reference: https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
    */
    private boolean isActiveNetwork(){

        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
