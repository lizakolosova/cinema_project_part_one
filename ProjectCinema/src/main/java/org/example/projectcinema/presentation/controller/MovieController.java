package org.example.projectcinema.presentation.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.MovieDTO;
import org.example.projectcinema.exceptions.MovieNotFoundException;
import org.example.projectcinema.presentation.converter.MovieViewModelToMovieConverter;
import org.example.projectcinema.presentation.viewmodel.MovieViewModel;
import org.example.projectcinema.service.MovieService;
import org.example.projectcinema.service.SessionHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final MovieViewModelToMovieConverter converter;
    private final SessionHistoryService sessionHistoryService;
    private static final Logger log = LoggerFactory.getLogger(MovieController.class);


    public MovieController(
            MovieService movieService,
            MovieViewModelToMovieConverter converter,
            SessionHistoryService sessionHistoryService) {
        this.movieService = movieService;
        this.converter = converter;
        this.sessionHistoryService = sessionHistoryService;
    }

    @GetMapping("/movies")
    public String getAllMovies(Model model, HttpSession session) {
        log.info("Fetching all movies");

        List<MovieDTO> movies = movieService.getAllMovies();

        model.addAttribute("movies", movies);
        sessionHistoryService.addPageVisit(session, "All movies Page");

        return "movies";
    }

    @PostMapping("/movies/filter")
    public String getFilteredMovies(
            @RequestParam(value = "genre", required = false) String genreInput,
            @RequestParam(value = "rating", required = false) Double rating,
            Model model) {

        log.info("Filtering movies by genre: {} and rating: {}", genreInput, rating);

        List<MovieDTO> movies = movieService.getFilteredMovies(genreInput, rating);
        model.addAttribute("movies", movies);

        return "movies";
    }

    @GetMapping("/addmovie")
    public String showAddMovieForm(Model model, HttpSession session) {
        log.info("Displaying form to add a new movie");

        model.addAttribute("movieViewModel", new MovieViewModel());
        sessionHistoryService.addPageVisit(session, "Add movie Page");

        return "addmovie";
    }

    @PostMapping("/addmovie")
    public String addMovie(
            @Valid @ModelAttribute("movieViewModel") MovieViewModel movieViewModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.warn("Validation failed for movie: {}", bindingResult.getFieldErrors());
            return "addmovie";
        }

        log.info("Adding new movie: {}", movieViewModel.getTitle());

        Movie movie = converter.convert(movieViewModel);

        movieService.save(movie);

        return "redirect:/movies";
    }

    @GetMapping("/movie-details/{id}")
    public String viewMovieDetails(@PathVariable Long id, Model model, HttpSession session) {
        try {
            MovieDTO movie = movieService.findById(id);

            model.addAttribute("movie", movie);
            sessionHistoryService.addPageVisit(session, "Movie details Page");

            return "movie-details";

        } catch (MovieNotFoundException ex) {
            log.error("Movie not found with id: {}", id);
            model.addAttribute("message", ex.getMessage());
            return "other-error";
        }
    }

    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        log.info("Deleting movie with id: {}", id);
        movieService.deleteById(id);
        return "redirect:/movies";
    }

    @GetMapping("/releaseDate")
    public String findByReleaseDateAfterForm(
            @RequestParam(required = false) LocalDate releaseDate,
            Model model,
            HttpSession session) {

        if (releaseDate != null) {
            log.info("Fetching movies released after: {}", releaseDate);
            List<MovieDTO> movies = movieService.findByReleaseDateAfter(releaseDate);
            model.addAttribute("movies", movies);
        }

        sessionHistoryService.addPageVisit(session, "Find Movie by Release Date Page");
        return "movies-by-date";
    }

    @PostMapping("/releaseDate")
    public String findByReleaseDate(
            @RequestParam LocalDate releaseDate,
            Model model,
            HttpSession session) {

        log.info("Searching movies released after: {}", releaseDate);

        List<MovieDTO> movies = movieService.findByReleaseDateAfter(releaseDate);
        model.addAttribute("movies", movies);
        sessionHistoryService.addPageVisit(session, "Find Movie by Release Date Page");

        return "movies-by-date";
    }
}