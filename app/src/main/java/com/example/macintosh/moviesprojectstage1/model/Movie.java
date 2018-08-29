package com.example.macintosh.moviesprojectstage1.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by macintosh on 27/08/2018.
 */

public class Movie {

    private String title;
    private int id;
    private int voteCount;
    private String imageUrl;


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

    public String getTitle() {
        return title;
    }

    public int getid() {
        return id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Bitmap getImage(){
        Bitmap bmp = BitmapFactory.decodeFile(imageUrl);
        return bmp;
    }
}
