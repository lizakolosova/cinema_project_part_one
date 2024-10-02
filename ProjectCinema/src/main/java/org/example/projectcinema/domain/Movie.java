package org.example.projectcinema.domain;

import java.time.LocalDate;

public class Movie {
    private String title;
    private LocalDate releaseDate;
    private double rating;
    private Genre genre;

    public Movie(String title, LocalDate releaseDate, double rating, Genre genre) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genre = genre;
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

    @Override
    public String toString() {
        return title + " (" + genre + ") - " + releaseDate + " - Rating: " + rating;
    }
}