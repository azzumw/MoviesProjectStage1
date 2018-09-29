package com.example.macintosh.moviesprojectstage1.model;


public class Movie{

    private String title;
    private int id;
    private int voteCount;
    private String imageUrl;
    private String plotSynopsis;
    private int plotAverage;
    private String releaseDate;
    private Boolean isFavourite = false;

    private Movie(String title){
        this.title = title;
    }

    private Movie(String title, int id){
        this(title);
        this.id=id;
    }

    private Movie(String title,int id, int voteCount){
        this(title,id);
        this.voteCount = voteCount;
    }

    private Movie(String title, int id, int voteCount, String imageUrl) {
        this(title,id,voteCount);
        this.imageUrl = imageUrl;
    }

    private Movie(String title, int id, int voteCount, String imageUrl, String releaseDate){
        this(title,id,voteCount,imageUrl);
        this.releaseDate = releaseDate;
    }

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
