package com.udacity.movietip.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.udacity.movietip.R;
import com.udacity.movietip.data.model.Movie;

public class DetailActivity extends AppCompatActivity {

    private static final String MOVIE_ITEM = "Movie Item";
    private static final int IMAGE_LOADING = R.drawable.ic_image_loading;
    private static final int BROKEN_IMAGE = R.drawable.ic_broken_image;

    /*
      Parcelable concepts applied from: https://www.youtube.com/watch?v=WBbsvqSu0is
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MOVIE_ITEM);

        String backdropUrl = movie.getBackdropUrl();
        String posterUrl = movie.getPosterUrl();
        String title = movie.getTitle();
        Float voteAverage = movie.getVoteAverage();
        Integer voteCount = movie.getVoteCount();
        String releaseDate = movie.getReleaseDate();
        String overview = movie.getOverview();

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
}
