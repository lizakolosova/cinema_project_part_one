package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class CinemaScreen {
    private int screenNumber;
    private Cinema cinema;
    private String screenType;
    private int size;
    private List<Movie> movies;

    public CinemaScreen(int screenNumber, Cinema cinema, String screenType, int size) {
        this.screenNumber = screenNumber;
        this.cinema = cinema;
        this.screenType = screenType;
        this.size = size;
        this.movies = new ArrayList<>();
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
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


    @Override
    public String toString() {
        return "Cinema Screen " + screenNumber + " - Type: " + screenType + ", Size: " + size;
    }
}