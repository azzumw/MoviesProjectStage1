package com.example.macintosh.moviesprojectstage1.database;

public class Review {

    private String review;
    private String author;

    public Review(String review,String author) {
        this.author = author;
        this.review = review;
    }

    public String getReview() {
        return review;
    }

    public String getAuthor() {
        return author;
    }
}
