package org.example.projectcinema.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDate releaseDate;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.")
    @DecimalMax(value = "10.0", message = "Rating must not exceed 10.")
    private double rating;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private String image;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<CinemaScreen> screens = new ArrayList<>();

    @ManyToMany(mappedBy = "movies", cascade = CascadeType.PERSIST)
    private List<Cinema> cinemas = new ArrayList<>();

    public Movie(Long id, String title, LocalDate releaseDate, double rating, Genre genre, String image) {
        this.id = id;
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
        this.screens = new ArrayList<>();
        this.cinemas = new ArrayList<>();
    }

    public Movie() {
    }


    public void addScreen(CinemaScreen screen) {
        if(!this.screens.contains(screen)) {
            this.screens.add(screen);
            screen.addMovie(this);
        }
    }

    public void addCinema(Cinema cinema) {
        if (!this.cinemas.contains(cinema)) {
            this.cinemas.add(cinema);
        }
    }

    @Override
    public String toString() {
        return title + " (" + genre + ") - " + releaseDate + " - Rating: " + rating;
    }
}