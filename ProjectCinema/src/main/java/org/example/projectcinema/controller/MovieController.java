package org.example.projectcinema.controller;

import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public String getAllMovies(Model model) {
        logger.info("Fetching all movies");
        List<Movie> movies = movieService.getAllMovies();
        logger.debug("Number of movies fetched: {}", movies.size());
        model.addAttribute("movies", movies);
        return "movies";
    }

    @PostMapping("/movies/filter")
    public String getFilteredMovies(@RequestParam(value = "genre", required = false) String genreInput,
                                    @RequestParam(value = "rating", required = false) Double rating,
                                    Model model) {
        logger.info("Filtering movies by genre: {} and rating: {}", genreInput, rating);
        Genre genre = null;
        if (genreInput != null && !genreInput.isEmpty()) {
            try {
                genre = Genre.valueOf(genreInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid genre provided: {}", genreInput);
                model.addAttribute("error", "Invalid genre: " + genreInput);
                return "movies";
            }
        }

        List<Movie> movies = movieService.getMoviesByGenreAndRating(genre, rating);
        logger.debug("Number of movies after filtering: {}", movies.size());
        model.addAttribute("movies", movies);
        return "movies";
    }

    @GetMapping("/addmovie")
    public String showAddMovieForm(Model model) {
        logger.info("Displaying form to add a new movie");
        model.addAttribute("movie", new Movie());
        return "addmovie";
    }

    @PostMapping("/addmovie")
    public String addMovie(Movie movie, Model model) {
        logger.info("Processing request to add movie: {}", movie.getTitle());

        if (movie.getTitle() == null || movie.getTitle().isEmpty()) {
            logger.warn("Movie title is empty. Cannot proceed with adding movie.");
            model.addAttribute("error", "Movie title cannot be empty");
            return "addmovie";
        }
        if (movie.getGenre() == null) {
            logger.warn("Movie genre is empty. Cannot proceed with adding movie.");
            model.addAttribute("error", "Movie genre cannot be empty");
            return "addmovie";
        }
        if (movie.getRating() < 0 || movie.getRating() > 10) {
            logger.warn("Invalid movie rating: {}. Rating must be between 0 and 10.", movie.getRating());
            model.addAttribute("error", "Movie rating must be between 0 and 10");
            return "addmovie";
        }

        try {
            logger.debug("Saving movie: {}", movie.getTitle());
            movieService.addMovie(movie);
        } catch (IllegalArgumentException e) {
            logger.error("Error while adding movie: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "addmovie";
        }

        logger.info("Movie added successfully: {}", movie.getTitle());
        return "redirect:/movies";
    }
    @GetMapping("/movie-details")
    public String getMovieDetails(@RequestParam String title, Model model) {
        logger.debug("Received title: {}", title);
        Movie movie = movieService.findByTitle(title);
        if (movie == null) {
            model.addAttribute("error", "Movie not found.");
            return "error-page";
        }
        model.addAttribute("movie", movie);
        return "movie-details";
    }
}

