package com.example.farisfathurrahman25.taskedapp;

public class Book {
    private String title;
    private String imageURL;
    private String author;
    private float rating;

    public Book() {
    }

    public Book(String title, String imageURL, String author, float rating) {
        this.title = title;
        this.imageURL = imageURL;
        this.author = author;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
