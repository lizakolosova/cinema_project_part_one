package org.example.projectcinema.domain;

import java.util.ArrayList;
import java.util.List;

public class CinemaScreen {
    private int Id;
    private int screenNumber;
    private Cinema cinema;
    private String screenType;
    private int size;
    private List<Movie> movies;

    public CinemaScreen(int screenNumber, String screenType, int size) {
        this.screenNumber = screenNumber;
        this.screenType = screenType;
        this.size = size;
    }

    public int getsId() {
        return Id;
    }

    public void setsId(int sId) {
        this.Id = sId;
    }

    public int getScreenNumber() {
        return screenNumber;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public String getScreenType() {
        return screenType;
    }

    public int getSize() {
        return size;
    }

    public List<Movie> getMovies() {
        return movies;
    }
public void setMovies(List<Movie> movies) {
        this.movies = movies;
}
    public void addMovie(Movie movie) {
        this.movies.add(movie);
        movie.addScreen(this);
    }


    @Override
    public String toString() {
        return "Cinema Screen " + screenNumber + " - Type: " + screenType + ", Size: " + size;
    }
}