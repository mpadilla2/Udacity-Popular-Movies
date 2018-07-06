package com.udacity.movietip.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "favorites")
public class Movie implements Parcelable {

    // TMDB api image sizes Reference: https://www.themoviedb.org/talk/53c11d4ec3a3684cf4006400?language=en
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";
    private static final String BACKDROP_SIZE = "w300";
    /*
     Using GSON: https://github.com/codepath/android_guides/wiki/Leveraging-the-Gson-Library
     If the class fields match the JSON keys, GSON will map automatically.
    */
    private String posterPath;
    private String overview;
    private String releaseDate;
    @PrimaryKey
    private Integer id;
    private String title;
    private String backdropPath;
    private Double popularity;
    private Integer voteCount;
    private Float voteAverage;

    @Ignore
    public Movie(){}

    public Movie(String posterPath, String overview, String releaseDate, Integer id, String title,
                 String backdropPath, Double popularity, Integer voteCount, Float voteAverage){
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }

    public void setPosterPath(String posterPath){
        this.posterPath = posterPath;
    }

    public String getPosterPath(){
        return posterPath;
    }

    public String getPosterUrl(){
        return IMAGE_BASE_URL + POSTER_SIZE + posterPath;
    }

    public void setOverview(String overview){
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setBackdropPath(String backdropPath){
        this.backdropPath = backdropPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getBackdropUrl(){
        return IMAGE_BASE_URL + BACKDROP_SIZE + backdropPath;
    }

    public void setPopularity(Double popularity){
        this.popularity = popularity;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setVoteCount(Integer voteCount){
        this.voteCount = voteCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteAverage(Float voteAverage){
        this.voteAverage = voteAverage;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    // Parcelable boilerplate code implemented with Android Parcelable Code Generator by Michal Charmas
    // Reference: https://developer.android.com/guide/components/activities/parcelables-and-bundles
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.backdropPath);
        dest.writeSerializable(this.popularity);
        dest.writeValue(this.voteCount);
        dest.writeSerializable(this.voteAverage);
    }

    private Movie(Parcel in) {
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.backdropPath = in.readString();
        this.popularity = (Double) in.readSerializable();
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.voteAverage = (Float) in.readSerializable();
        String posterUrl = in.readString();
        String backdropUrl = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {

        // convert the parcel back to the current object
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
