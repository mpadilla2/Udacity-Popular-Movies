package com.udacity.movietip.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.udacity.movietip.R;
import com.udacity.movietip.data.model.Movie;

public class DetailActivity extends AppCompatActivity {

    private static final String MOVIE_ITEM = "Movie Item";

    /*
      Parcelable concepts applied from: https://www.youtube.com/watch?v=WBbsvqSu0is
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MOVIE_ITEM);

        Integer id = movie.getId();
        String backdropUrl = movie.getBackdropUrl();
        String posterUrl = movie.getPosterUrl();
        String title = movie.getTitle();
        Float voteAverage = movie.getVoteAverage();
        Integer voteCount = movie.getVoteCount();
        String releaseDate = movie.getReleaseDate();
        String overview = movie.getOverview();
        Double popularity = movie.getPopularity();

        // Reference: https://developer.android.com/guide/topics/resources/runtime-changes
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            ImageView backdropImageView = findViewById(R.id.detail_movie_backdrop_imageView);
            loadImages(this, backdropUrl, backdropImageView);
        }

        ImageView posterImageView = findViewById(R.id.detail_movie_poster_imageView);
        loadImages(this, posterUrl, posterImageView);

        TextView movieNameTextView = findViewById(R.id.detail_movie_name_textView);
        movieNameTextView.setText(title);

        TextView releaseDateTextView = findViewById(R.id.detail_movie_release_date);
        releaseDateTextView.setText(releaseDate);

        TextView overViewTextView = findViewById(R.id.detail_movie_overview);
        overViewTextView.setText(overview);

        RatingBar ratingBar = findViewById(R.id.movie_detail_vote_average);
        ratingBar.setRating(voteAverage);

        TextView voteCountTextview = findViewById(R.id.detail_movie_vote_count_textView);
        voteCountTextview.setText(String.valueOf(voteCount));

        TextView popularityTextView = findViewById(R.id.detail_movie_popularity_rank_textView);
        popularityTextView.setText(String.valueOf(Math.ceil(popularity)));

    }

    private void loadImages(Context context, String imageUrl, ImageView imageView){

        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .optionalCenterCrop()
                        .placeholder(R.drawable.ic_image_loading)
                        .error(R.drawable.ic_broken_image))
                .into(imageView);
    }
}
