package com.udacity.movietip.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.udacity.movietip.R;
import com.udacity.movietip.data.adapters.ReviewsAdapter;
import com.udacity.movietip.data.adapters.TrailersAdapter;
import com.udacity.movietip.data.adapters.TrailersAdapter.ItemClickListener;

import com.udacity.movietip.data.model.Movie;
import com.udacity.movietip.data.model.MovieViewModel;
import com.udacity.movietip.data.model.Reviews;
import com.udacity.movietip.data.model.ReviewsViewModel;
import com.udacity.movietip.data.model.Trailers;
import com.udacity.movietip.data.model.TrailersViewModel;
import com.udacity.movietip.ui.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity
        extends AppCompatActivity{

    private static final String EXTRA_MOVIE_ITEM = "Movie Item";
    private static final String SAVED_INSTANCE_MOVIE_ITEM = "Saved Movie";
    private static final String SAVED_INSTANCE_REVIEWS_ITEM = "Saved Reviews";
    private static final String SAVED_INSTANCE_TRAILERS_ITEM = "Saved Trailers";
    private static final int IMAGE_LOADING = R.drawable.ic_image_loading;
    private static final int BROKEN_IMAGE = R.drawable.ic_broken_image;

    private Context mContext;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private ImageView mPosterImageView;
    private ImageView mBackdropImageView;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private TextView mReleaseDateTextView;
    private TextView mOverViewTextView;
    private RatingBar mRatingBar;
    private TextView mVoteCountTextview;
    private android.support.v7.widget.Toolbar mToolbar;
    private ImageButton mFavoritesButton;
    private RecyclerView mTrailersRecyclerView;
    private RecyclerView mReviewsRecyclerView;

    private Movie movie;
    private ArrayList<Trailers> trailersList;
    private ArrayList<Reviews> reviewsList;
    private MovieViewModel mMovieViewModel;
    private TrailersViewModel mTrailersViewModel;
    private ReviewsViewModel mReviewsViewModel;


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

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mTrailersViewModel = ViewModelProviders.of(this).get(TrailersViewModel.class);
        mReviewsViewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SAVED_INSTANCE_MOVIE_ITEM)) {
                movie = savedInstanceState.getParcelable(SAVED_INSTANCE_MOVIE_ITEM);
            }
            if (savedInstanceState.containsKey(SAVED_INSTANCE_REVIEWS_ITEM)) {
                reviewsList = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_REVIEWS_ITEM);
            }
            if (savedInstanceState.containsKey(SAVED_INSTANCE_TRAILERS_ITEM)) {
                trailersList = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_TRAILERS_ITEM);
            }

            if (trailersList != null) {
                mTrailersAdapter.setTrailersList(trailersList);
            }
            if (reviewsList != null){
                mReviewsAdapter.setReviewsList(reviewsList);
            }

        } else {

            trailersList = new ArrayList<>();
            reviewsList = new ArrayList<>();

            Intent intent = getIntent();

            if (intent != null && intent.hasExtra(EXTRA_MOVIE_ITEM)){
                movie = intent.getParcelableExtra(EXTRA_MOVIE_ITEM);
            }

            loadTrailers(movie.getId());
            loadReviews(movie.getId());
        }

        populateUI(movie);

        mFavoritesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mMovieViewModel.toggleFavorite(movie);
            }
        });

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_INSTANCE_MOVIE_ITEM, movie);
        outState.putParcelableArrayList(SAVED_INSTANCE_REVIEWS_ITEM, reviewsList);
        outState.putParcelableArrayList(SAVED_INSTANCE_TRAILERS_ITEM, trailersList);
        super.onSaveInstanceState(outState);
    }

    private void initRecyclerViews() {

        RecyclerView.LayoutManager mTrailersLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(mTrailersLayoutManager);
        mTrailersRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));
        mTrailersRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mReviewsLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
        mReviewsRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));
        mReviewsRecyclerView.setHasFixedSize(true);

        final List<Trailers> trailersList = new ArrayList<>();
        mTrailersAdapter = new TrailersAdapter(this, trailersList, new ItemClickListener() {
            @Override
            public void onItemClick(int clickedItemIndex) {
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
        });

        final List<Reviews> reviewsList = new ArrayList<>();
        mReviewsAdapter = new ReviewsAdapter(this, reviewsList, new ReviewsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int clickedItemIndex) {
                // grab the clicked review
                Reviews review = reviewsList.get(clickedItemIndex);

                Intent reviewIntent = new Intent(Intent.ACTION_VIEW);
                reviewIntent.setData(Uri.parse(review.getUrl()));

                Intent chooser = Intent.createChooser(reviewIntent, "Choose Browser");

                if (reviewIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(chooser);
                }
            }
        });
    }


    private void initViews() {

        mPosterImageView = findViewById(R.id.detail_movie_poster_imageView);
        mBackdropImageView = findViewById(R.id.detail_movie_backdrop_imageView);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        mReleaseDateTextView = findViewById(R.id.detail_movie_release_date);
        mOverViewTextView = findViewById(R.id.detail_movie_overview);
        mRatingBar = findViewById(R.id.movie_detail_vote_average);
        mVoteCountTextview = findViewById(R.id.detail_movie_vote_count_textView);
        mToolbar = findViewById(R.id.toolbar);
        mTrailersRecyclerView = findViewById(R.id.detail_movie_trailers_recyclerview);
        mReviewsRecyclerView = findViewById(R.id.detail_movie_reviews_recyclerview);
        mFavoritesButton = findViewById(R.id.detail_movie_favorites_button);

    }


    private void populateUI(Movie movie){

        if (movie == null) {
            return;
        }

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        mCollapsingToolbar.setTitle(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mOverViewTextView.setText(movie.getOverview());
        mRatingBar.setRating(movie.getVoteAverage());
        mVoteCountTextview.setText(String.valueOf(movie.getVoteCount()));

        // Reference: https://developer.android.com/guide/topics/resources/runtime-changes
        if(isPortrait) {
            setConstraintBackground(movie.getPosterUrl());
        } else {
            setConstraintBackground(movie.getBackdropUrl());
            loadImageIntoView(this, movie.getPosterUrl(), mPosterImageView);
        }

        loadImageIntoView(this, movie.getBackdropUrl(), mBackdropImageView);
    }


    private void setConstraintBackground(String imageUrl) {

        final ConstraintLayout scrollingConstraint = findViewById(R.id.scrolling_constraint);

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


    private void loadTrailers(int movieId){
        mTrailersViewModel.getAllTrailers(movieId).observe(this, new Observer<List<Trailers>>() {
            @Override
            public void onChanged(@Nullable List<Trailers> trailersList) {
                Log.d("DetailActivity", "Updating list of Trailers from TMDB api LiveData in ViewModel");
                mTrailersAdapter.setTrailersList(trailersList);
            }
        });
    }


    private void loadReviews(int movieId){
        mReviewsViewModel.getAllReviews(movieId).observe(this, new Observer<List<Reviews>>() {
            @Override
            public void onChanged(@Nullable List<Reviews> reviews) {
                Log.d("DetailActivity", "Updating list of Reviews from TMDB api LiveData in ViewModel");
                mReviewsAdapter.setReviewsList(reviews);
            }
        });

    }
}
