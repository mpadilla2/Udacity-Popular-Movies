package com.udacity.movietip.data.model;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.udacity.movietip.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.content.res.Resources.*;

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
    private Boolean adult;
    private String overview;
    private String releaseDate;
    private List<Integer> genreIds = new ArrayList<Integer>();
    private Integer id;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private Number popularity;
    private Integer voteCount;
    private Boolean video;
    private BigDecimal voteAverage;
    private String posterUrl;
    private String backdropUrl;


    public Movie(){}

    public Movie(String posterPath, Boolean isAdult, String plotSynopsis, String releaseDate,
                 List<Integer> genreIds, Integer id, String originalTitle, String originalLanguage, String title,
                 String backdropPath, Number popularity, Integer voteCount, Boolean hasVideo, BigDecimal voteAverage){
        this.posterPath = posterPath;
        this.adult = isAdult;
        this.overview = plotSynopsis;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = hasVideo;
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

    public void setAdult(Boolean isAdult){
        this.adult = isAdult;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setOverview(String plotSynopsis){
        this.overview = plotSynopsis;
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

    public void setGenreIds(List<Integer> genreIds){
        this.genreIds = genreIds;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setOriginalTitle(String originalTitle){
        this.originalTitle = originalTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalLanguage(String originalLanguage){
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
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

    public void setPopularity(Number popularity){
        this.popularity = popularity;
    }

    public Number getPopularity() {
        return popularity;
    }

    public void setVoteCount(Integer voteCount){
        this.voteCount = voteCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVideo(Boolean hasVideo){
        this.video = hasVideo;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVoteAverage(BigDecimal voteAverage){
        this.voteAverage = voteAverage;
    }

    public BigDecimal getVoteAverage() {
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
        dest.writeValue(this.adult);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeList(this.genreIds);
        dest.writeValue(this.id);
        dest.writeString(this.originalTitle);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.title);
        dest.writeString(this.backdropPath);
        dest.writeSerializable(this.popularity);
        dest.writeValue(this.voteCount);
        dest.writeValue(this.video);
        dest.writeSerializable(this.voteAverage);
    }

    protected Movie(Parcel in) {
        this.posterPath = in.readString();
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.genreIds = new ArrayList<Integer>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.title = in.readString();
        this.backdropPath = in.readString();
        this.popularity = (Number) in.readSerializable();
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.voteAverage = (BigDecimal) in.readSerializable();
        this.posterUrl = in.readString();
        this.backdropUrl = in.readString();
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
