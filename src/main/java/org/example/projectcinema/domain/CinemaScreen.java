package org.example.projectcinema.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
public class CinemaScreen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int screenNumber;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    private String screenType;
    private int size;

    @ManyToMany(mappedBy = "screens")
    private List<Movie> movies = new ArrayList<>();

    public CinemaScreen() {

    }

    public CinemaScreen(int screenNumber, Cinema cinema, String screenType, int size) {
        this.screenNumber = screenNumber;
        this.cinema = cinema;
        this.screenType = screenType;
        this.size = size;
        this.movies = new ArrayList<>();
    }
    
    public CinemaScreen(int screenNumber, String screenType, int size) {
        this.screenNumber = screenNumber;
        this.screenType = screenType;
        this.size = size;
        this.movies = new ArrayList<>();
    }


    public void addMovie(Movie movie) {
        if (!movies.contains(movie)) {
            this.movies.add(movie);
        }
        if(!movie.getScreens().contains(this)) {
            movie.getScreens().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CinemaScreen screen)) return false;
        return id != null && id.equals(screen.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Cinema Screen " + screenNumber + " - Type: " + screenType + ", Size: " + size;
    }

}
