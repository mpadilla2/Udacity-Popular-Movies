package com.udacity.movietip.ui.activities;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.udacity.movietip.R;
import com.udacity.movietip.data.model.Movie;

import java.math.BigDecimal;

public class DetailActivity extends AppCompatActivity {


    /*
      Parcelable concepts applied from: https://www.youtube.com/watch?v=WBbsvqSu0is
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("Movie Item");

        int id = movie.getId();
        String backdropUrl = movie.getBackdropUrl();
        String posterUrl = movie.getPosterUrl();
        String title = movie.getTitle();
        BigDecimal voteAverage = movie.getVoteAverage();
        String releaseDate = movie.getReleaseDate();
        String overview = movie.getOverview();

        ImageView backdropImageView = findViewById(R.id.detail_movie_backdrop_imageView);
        Glide.with(this)
                .load(backdropUrl)
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_loading)
                        .error(R.drawable.ic_broken_image))
                .into(backdropImageView);
    }
}
