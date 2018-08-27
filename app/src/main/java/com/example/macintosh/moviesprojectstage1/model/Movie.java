package com.example.macintosh.moviesprojectstage1.model;

/**
 * Created by macintosh on 27/08/2018.
 */

public class Movie {

    private String title;
    private int id;


    public Movie(String title){
        this.title = title;
    }
    public Movie(String title, int id){
        this(title);
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public int getid() {
        return id;
    }
}
