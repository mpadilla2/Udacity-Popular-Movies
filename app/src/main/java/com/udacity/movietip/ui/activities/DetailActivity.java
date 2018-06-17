package com.udacity.movietip.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.udacity.movietip.R;
import com.udacity.movietip.data.adapters.ReviewsAdapter;
import com.udacity.movietip.data.adapters.TrailersAdapter;
import com.udacity.movietip.data.adapters.TrailersAdapter.ItemClickListener;
import com.udacity.movietip.data.model.Movie;
import com.udacity.movietip.data.model.MoviesIndexed;
import com.udacity.movietip.data.model.Reviews;
import com.udacity.movietip.data.model.ReviewsIndexed;
import com.udacity.movietip.data.model.Trailers;
import com.udacity.movietip.data.model.TrailersIndexed;
import com.udacity.movietip.data.remote.ApiService;
import com.udacity.movietip.data.utils.ApiUtils;
import com.udacity.movietip.ui.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String MOVIE_ITEM = "Movie Item";
    private static final int IMAGE_LOADING = R.drawable.ic_image_loading;
    private static final int BROKEN_IMAGE = R.drawable.ic_broken_image;
    Context mContext = this;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;

    /*
      Parcelable concepts applied from: https://www.youtube.com/watch?v=WBbsvqSu0is
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MOVIE_ITEM);

        Integer movieId = movie.getId();
        Boolean hasVideo = movie.getVideo();
        String backdropUrl = movie.getBackdropUrl();
        String posterUrl = movie.getPosterUrl();
        String title = movie.getTitle();
        Float voteAverage = movie.getVoteAverage();
        Integer voteCount = movie.getVoteCount();
        String releaseDate = movie.getReleaseDate();
        String overview = movie.getOverview();

        loadTrailers(movieId);
        loadReviews(movieId);

        // Reference: https://developer.android.com/guide/topics/resources/runtime-changes
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // if it's portrait, set the constraint background to the poster
            setConstraintBackground(posterUrl);
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // if it's landscape, set the constraint background to the backdrop
            setConstraintBackground(backdropUrl);

            ImageView posterImageView = findViewById(R.id.detail_movie_poster_imageView);
            loadImages(this, posterUrl, posterImageView);
        }

        ImageView backdropImageView = findViewById(R.id.detail_movie_backdrop_imageView);
        loadImages(this, backdropUrl, backdropImageView);

        // Set the toolbar title to the movie title
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title);

        TextView releaseDateTextView = findViewById(R.id.detail_movie_release_date);
        releaseDateTextView.setText(releaseDate);

        TextView overViewTextView = findViewById(R.id.detail_movie_overview);
        overViewTextView.setText(overview);

        RatingBar ratingBar = findViewById(R.id.movie_detail_vote_average);
        ratingBar.setRating(voteAverage);

        TextView voteCountTextview = findViewById(R.id.detail_movie_vote_count_textView);
        voteCountTextview.setText(String.valueOf(voteCount));

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView mTrailersRecyclerView = findViewById(R.id.detail_movie_trailers_recyclerview);
        mTrailersRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mTrailersLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(mTrailersLayoutManager);
        mTrailersRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));

        RecyclerView mReviewsRecyclerView = findViewById(R.id.detail_movie_reviews_recyclerview);
        mReviewsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mReviewsLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
        mReviewsRecyclerView.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));

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

        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
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

    private void loadImages(Context context, String imageUrl, ImageView imageView){

        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .optionalCenterCrop()
                        .placeholder(IMAGE_LOADING)
                        .error(BROKEN_IMAGE))
                .into(imageView);
    }

    private void loadTrailers(Integer movieId){

        ApiService mService = ApiUtils.getApiService();
        mService.getTrailers(movieId).enqueue(new Callback<TrailersIndexed>() {
            @Override
            public void onResponse(@NonNull Call<TrailersIndexed> call, @NonNull Response<TrailersIndexed> response) {
                assert response.body() != null;
                List<Trailers> trailersList = response.body() != null ? Objects.requireNonNull(response.body()).getResults() : null;
                Toast.makeText(getBaseContext(), "Retrieval of TRAILERS successful!", Toast.LENGTH_SHORT).show();
                mTrailersAdapter.setTrailersList(trailersList);
            }

            @Override
            public void onFailure(@NonNull Call<TrailersIndexed> call, @NonNull Throwable t) {
                Toast.makeText(mContext, getString(R.string.internet_status), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadReviews(Integer movieId){

        ApiService mService = ApiUtils.getApiService();
        mService.getReviews(movieId).enqueue(new Callback<ReviewsIndexed>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsIndexed> call, @NonNull Response<ReviewsIndexed> response) {
                assert response.body() != null;
                List<Reviews> reviewsList = response.body() != null ? Objects.requireNonNull(response.body()).getResults() : null;
                Toast.makeText(getBaseContext(), "Retrieval of REVIEWS successful!", Toast.LENGTH_SHORT).show();
                mReviewsAdapter.setReviewsList(reviewsList);
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsIndexed> call, @NonNull Throwable t) {
                Toast.makeText(mContext, getString(R.string.internet_status), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
