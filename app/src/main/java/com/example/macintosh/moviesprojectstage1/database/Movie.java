package com.example.macintosh.moviesprojectstage1.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movie")
public class Movie{

    @PrimaryKey
    private int id;
    private String title;
    private int voteCount;
    private String imageUrl;
    private String plotSynopsis;
    private int plotAverage;
    private String releaseDate;

    @Ignore
    private Boolean isFavourite = false;

    @Ignore
    private Movie(String title){
        this.title = title;
    }

    @Ignore
    private Movie(String title, int id){
        this(title);
        this.id=id;
    }

    @Ignore
    private Movie(String title,int id, int voteCount){
        this(title,id);
        this.voteCount = voteCount;
    }

    @Ignore
    private Movie(String title, int id, int voteCount, String imageUrl) {
        this(title,id,voteCount);
        this.imageUrl = imageUrl;
    }

    @Ignore
    private Movie(String title, int id, int voteCount, String imageUrl, String releaseDate){
        this(title,id,voteCount,imageUrl);
        this.releaseDate = releaseDate;
    }

    @Ignore
    private Movie(String title, int id, int voteCount, String imageUrl, String releaseDate, int plotAverage){
        this(title,id,voteCount,imageUrl,releaseDate);
        this.plotAverage = plotAverage;
    }

    public Movie(String title, int id, int voteCount, String imageUrl, String releaseDate, int plotAverage, String plotSynopsis){
        this(title,id,voteCount,imageUrl,releaseDate,plotAverage);
        this.plotSynopsis = plotSynopsis;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean favourite){
        this.isFavourite = favourite;
    }

    public String getTitle() {
        return title;
    }

    public int getid() {
        return id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getImage(){

        return imageUrl;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public int getPlotAverage() {
        return plotAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


}
