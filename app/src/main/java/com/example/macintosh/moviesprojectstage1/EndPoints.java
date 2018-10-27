package com.example.macintosh.moviesprojectstage1;


public enum EndPoints {

    REVIEWS("reviews"), VIDEOS("videos");

    String type;

    EndPoints(String type) {
        this.type = type;
    }


    public String getType(){
        return type;
    }
}
