package org.example.projectcinema.controller;

import org.example.projectcinema.domain.Movie;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class MovieView {

    public void showAllMovies(Model model, List<Movie> movies) {
        model.addAttribute("movies", movies);
    }

    public void showFilteredMovies(Model model, List<Movie> movies) {
        model.addAttribute("movies", movies);
    }

    public void showMessage(Model model, String message) {
        model.addAttribute("message", message);
    }
}