package org.example.projectcinema.domain;


import jakarta.persistence.*;
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

    private double rating;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private String image;

    @ManyToMany
    @JoinTable(
            name = "movie_screens",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "screen_id")
    )
    private List<CinemaScreen> screens = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "cinema_movies",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "cinema_id")
    )
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
        }
        if(!screen.getMovies().contains(this)) {
            screen.getMovies().add(this);
        }
    }

    public void addCinema(Cinema cinema) {
        if (!this.cinemas.contains(cinema)) {
            this.cinemas.add(cinema);
        }
        if (!cinema.getMovies().contains(this)) {
            cinema.getMovies().add(this);
        }
    }

    public void removeCinema(Cinema cinema) {
        this.cinemas.remove(cinema);
        cinema.getMovies().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie movie)) return false;
        return id != null && id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return title + " (" + genre + ") - " + releaseDate + " - Rating: " + rating;
    }
}