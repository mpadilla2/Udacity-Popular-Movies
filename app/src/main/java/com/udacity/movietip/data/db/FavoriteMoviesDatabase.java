package com.udacity.movietip.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.udacity.movietip.data.model.Movie;

// Singleton pattern with lazy instantiation
@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class FavoriteMoviesDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "movies.db";
    private static FavoriteMoviesDatabase INSTANCE;

    public abstract FavoriteMoviesDAO favoriteMoviesDAO();

    /*
       checks to see if db instance exists first before creating db instance.
       only ever instantiates db if it doesn't exist (lazy instantiation)
       returns: the new instance if it didn't yet exist OR the existing instance.
       made thread safe by using synchronization to lock/block the INSTANCE resource.
    */
    public static FavoriteMoviesDatabase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (FavoriteMoviesDatabase.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteMoviesDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
