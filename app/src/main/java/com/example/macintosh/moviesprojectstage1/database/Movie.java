package com.example.macintosh.moviesprojectstage1.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "movie")
public class Movie implements Parcelable{

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

    @Ignore
    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        voteCount = in.readInt();
        imageUrl = in.readString();
        plotSynopsis = in.readString();
        plotAverage = in.readInt();
        releaseDate = in.readString();
        byte tmpIsFavourite = in.readByte();
        isFavourite = tmpIsFavourite == 0 ? null : tmpIsFavourite == 1;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean favourite){
        this.isFavourite = favourite;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getImageUrl(){

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

    public void setTitle(String title){
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(voteCount);
        dest.writeString(imageUrl);
        dest.writeString(plotSynopsis);
        dest.writeInt(plotAverage);
        dest.writeString(releaseDate);
        dest.writeByte((byte) (isFavourite == null ? 0 : isFavourite ? 1 : 2));
    }
}
