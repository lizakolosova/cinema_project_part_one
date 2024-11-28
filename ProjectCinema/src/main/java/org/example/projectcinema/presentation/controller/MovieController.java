package org.example.projectcinema.presentation.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.presentation.converters.MovieViewModelToMovieConverter;
import org.example.projectcinema.presentation.viewmodels.MovieViewModel;
import org.example.projectcinema.service.MovieService;
import org.example.projectcinema.service.SessionHistoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Controller
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;
    private final MovieViewModelToMovieConverter converter;
    private final SessionHistoryServiceImpl sessionHistoryService;

    @Autowired
    public MovieController(MovieService movieService, MovieViewModelToMovieConverter converter, SessionHistoryServiceImpl sessionHistoryService) {
        this.movieService = movieService;
        this.converter = converter;
        this.sessionHistoryService = sessionHistoryService;
    }

    @GetMapping("/movies")
    public String getAllMovies(Model model, HttpSession session) {
        logger.info("Fetching all movies");
        List<Movie> movies = movieService.getAllMovies();
        logger.debug("Number of movies fetched: {}", movies.size());
        model.addAttribute("movies", movies);
        sessionHistoryService.addPageVisit(session,"All movies Page");
        return "movies";
    }

    @PostMapping("/movies/filter")
    public String getFilteredMovies(@RequestParam(value = "genre", required = false) String genreInput,
                                    @RequestParam(value = "rating", required = false) Double rating,
                                    Model model) {
        logger.info("Filtering movies by genre: {} and rating: {}", genreInput, rating);
            List<Movie> movies = movieService.getFilteredMovies(genreInput, rating);
            model.addAttribute("movies", movies);
            logger.debug("Number of movies after filtering: {}", movies.size());
        return "movies";
    }


    @GetMapping("/addmovie")
    public String showAddMovieForm(Model model, HttpSession session) {
        logger.info("Displaying form to add a new movie");
        model.addAttribute("movieViewModel", new MovieViewModel());
        sessionHistoryService.addPageVisit(session,"Add movie Page");
        return "addmovie";
    }

    @PostMapping("/addmovie")
    public String addMovie(
            @Valid @ModelAttribute("movieViewModel") MovieViewModel movieViewModel,
            BindingResult bindingResult,
            Model model) {
        logger.info("Processing request to add movie: {}", movieViewModel.getTitle());
        if (bindingResult.hasErrors()) {
            logger.warn("Validation failed for movie: {}", bindingResult.getFieldErrors());
            return "addmovie";
        }
        Movie movie = converter.convert(movieViewModel);
            logger.debug("Saving movie: {}", movieViewModel.getTitle());
            movieService.addMovie(movie);
        logger.info("Movie added successfully: {}", movieViewModel.getTitle());
        return "redirect:/movies";
    }

@GetMapping("/movie-details/{id}")
public String viewMovieDetails(@PathVariable int id, Model model, HttpSession session) {
    Movie movie = movieService.findByIdWithCinemas(id);
    if (movie == null) {
        model.addAttribute("error", "Movie not found");
        return "error-page";
    }
    model.addAttribute("movie", movie);
    sessionHistoryService.addPageVisit(session, "Movie details Page");
    return "movie-details";
}
    @GetMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable int id, RedirectAttributes redirectAttributes) {
        movieService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Movie deleted successfully.");
        return "redirect:/movies";
    }
}

