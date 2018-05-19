package com.udacity.movietip.models;

import java.util.ArrayList;
import java.util.List;

public class MoviesResultsModel {

        /*
    Using GSON: https://github.com/codepath/android_guides/wiki/Leveraging-the-Gson-Library
    If the class fields match the JSON keys, GSON will map automatically.
    */

    /*
    id integer
    posterPath string or null
    adult boolean
    overview string
    releaseDate string
    genreIds array[integer]
    originalTitle string
    originalLanguage string
    title string
    backdropPath string or null
    popularity number
    voteCount integer
    video boolean
    voteAverage number
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
    private Number voteAverage;

    public MoviesResultsModel(){}

    public MoviesResultsModel(String posterPath, Boolean isAdult, String plotSynopsis, String releaseDate,
                              List<Integer> genreIds, Integer id, String originalTitle, String originalLanguage, String title,
                              String backdropPath, Number popularity, Integer voteCount, Boolean hasVideo, Number voteAverage){
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

    public void setVoteAverage(Number voteAverage){
        this.voteAverage = voteAverage;
    }

    public Number getVoteAverage() {
        return voteAverage;
    }

}
