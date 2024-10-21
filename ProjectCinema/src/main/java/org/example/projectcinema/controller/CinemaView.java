package org.example.projectcinema.controller;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.Movie;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class CinemaView {

    public void showMenu() {
    }

    public void showAllCinemas(Model model, List<Cinema> cinemas) {
        model.addAttribute("cinemas", cinemas);
    }

    public void showFilteredCinemas(Model model, List<Cinema> cinemas) {
        model.addAttribute("cinemas", cinemas);
    }

    public void showAllMovies(Model model, List<Movie> movies) {
        model.addAttribute("movies", movies);
    }

    public void showFilteredMovies(Model model, List<Movie> movies) {
        model.addAttribute("movies", movies);
    }

    public String getGenreInput(String genre) {
        return genre;
    }

    public Double getRatingInput(String ratingInput) {
        if (ratingInput.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(ratingInput);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public int getMinCapacity(String capacityInput) {
        try {
            return Integer.parseInt(capacityInput);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void showMessage(String message, Model model) {
        model.addAttribute("message", message);
    }
}