package org.example.projectcinema.controller;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.service.CinemaService;
import org.example.projectcinema.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CinemaController {

    private final CinemaService cinemaService;
    private final MovieService movieService;
    private final CinemaView cinemaView;

    @Autowired
    public CinemaController(CinemaService cinemaService, MovieService movieService, CinemaView cinemaView) {
        this.cinemaService = cinemaService;
        this.movieService = movieService;
        this.cinemaView = cinemaView;
    }

    @GetMapping("/cinemas")
    public String getAllCinemas(Model model) {
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        cinemaView.showAllCinemas(model, cinemas);
        return "cinemas";
    }

    @PostMapping("/cinemas/filter")
    public String getCinemasByCapacity(@RequestParam("minCapacity") int minCapacity, Model model) {
        model.addAttribute("cinemas", cinemaService.getCinemasByCapacity(minCapacity));
        return "cinemas";
    }

    @GetMapping("/movies")
    public String getAllMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies";
    }

    @PostMapping("/movies/filter")
    public String getMoviesByGenreAndRating(@RequestParam("genre") String genre,
                                            @RequestParam("rating") Double rating,
                                            Model model) {
        List<Movie> filteredMovies = movieService.getMoviesByGenreAndRating(genre, rating);
        if (filteredMovies.isEmpty()) {
            model.addAttribute("message", "No movies found for the selected genre and rating.");
        } else {
            model.addAttribute("movies", filteredMovies);
        }
        return "movies";
    }
}