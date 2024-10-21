package org.example.projectcinema.controller;

import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final MovieView movieView;

    @Autowired
    public MovieController(MovieService movieService, MovieView movieView) {
        this.movieService = movieService;
        this.movieView = movieView;
    }

    @GetMapping("/movies")
    public String getAllMovies(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        movieView.showAllMovies(model, movies);
        return "movies";
    }

    @PostMapping("/movies/filter")
    public String getFilteredMovies(@RequestParam("genre") String genre,
                                    @RequestParam("rating") Double rating,
                                    Model model) {
        List<Movie> movies = movieService.getMoviesByGenreAndRating(genre, rating);
        movieView.showFilteredMovies(model, movies);
        return "movies";
    }
}
