package com.example.macintosh.moviesprojectstage1.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by macintosh on 27/08/2018.
 */

public class Movie{

    private String title;
    private int id;
    private int voteCount;
    private String imageUrl;
//    private String imageurl;
    private String plotSynopsis;
    private int plotAverage;
    private String releaseDate;

    public Movie(String title){
        this.title = title;
    }

    public Movie(String title, int id){
        this(title);
        this.id=id;
    }

    public Movie(String title,int id, int voteCount){
        this(title,id);
        this.voteCount = voteCount;
    }

    public Movie(String title, int id, int voteCount, String imageUrl) {
        this(title,id,voteCount);
        this.imageUrl = imageUrl;
    }

    public Movie(String title, int id, int voteCount, String imageUrl, String releaseDate){
        this(title,id,voteCount,imageUrl);
        this.releaseDate = releaseDate;
    }

    public Movie(String title, int id, int voteCount, String imageUrl, String releaseDate, int plotAverage){
        this(title,id,voteCount,imageUrl,releaseDate);
        this.plotAverage = plotAverage;
    }

    public Movie(String title, int id, int voteCount, String imageUrl, String releaseDate, int plotAverage, String plotSynopsis){
        this(title,id,voteCount,imageUrl,releaseDate,plotAverage);
        this.plotSynopsis = plotSynopsis;
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
