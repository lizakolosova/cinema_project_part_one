package org.example.projectcinema.domain;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Movie {
    private int Id;
    private String title;
    private LocalDate releaseDate;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.")
    @DecimalMax(value = "10.0", message = "Rating must not exceed 10.")
    private double rating;

    private Genre genre;
    private String image;
    private List<CinemaScreen> screens  = new ArrayList<>();
    private List<Cinema> cinemas = new ArrayList<>();

    public Movie(int Id, String title, LocalDate releaseDate, double rating, Genre genre, String image) {
        this.Id = Id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genre = genre;
        this.image = image;
    }

    public Movie(String title, LocalDate releaseDate, double rating, Genre genre, String image) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genre = genre;
        this.image = image;
    }
    public Movie() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public void addScreen(CinemaScreen screen) {
        screens.add(screen);
    }

    public List<Cinema> getCinemas() {
        return cinemas;
    }

    public void setCinemas(List<Cinema> cinemas) {
        this.cinemas = cinemas;
    }

    @Override
    public String toString() {
        return title + " (" + genre + ") - " + releaseDate + " - Rating: " + rating;
    }
}