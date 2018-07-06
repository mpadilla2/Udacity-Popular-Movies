package com.udacity.movietip.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.udacity.movietip.data.model.Movie;

import java.util.ArrayList;
import java.util.List;


/*
  Reference: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0
  Reference: https://developer.android.com/jetpack/docs/guide
  Reference: https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929
 */
@Dao
public interface FavoriteMoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM favorites WHERE id = :movieId")
    LiveData<Movie> getMovieById(Integer movieId);

    @Query("SELECT * from favorites ORDER BY title ASC")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT count(*) from favorites WHERE id = :movieId")
    Integer getMovieCount(Integer movieId);

}
