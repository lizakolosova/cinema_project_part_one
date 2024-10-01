package org.example.presentation;

import org.example.domain.Cinema;
import org.example.domain.Movie;

import java.util.List;


public interface CinemaView {
    void showMenu();
    int getUserChoice();
    void showAllCinemas(List<Cinema> cinemas);
    void showFilteredCinemas(List<Cinema> cinemas);
    void showAllMovies(List<Movie> movies);
    void showFilteredMovies(List<Movie> movies);
    void showMessage(String message);
    int getMinCapacity();
    String getGenreInput();
    Double getRatingInput();
}