package org.example.projectcinema.domain;

import java.time.LocalDate;

public class Movie {
    private String title;
    private LocalDate releaseDate;
    private double rating;
    private Genre genre;
    private String image;

    public Movie(String title, LocalDate releaseDate, double rating, Genre genre, String image) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genre = genre;
        this.image = image;
    }
    public Movie() {
    }

    public Genre getGenre() {
        return genre;
    }

    public double getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }


    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return title + " (" + genre + ") - " + releaseDate + " - Rating: " + rating;
    }
}