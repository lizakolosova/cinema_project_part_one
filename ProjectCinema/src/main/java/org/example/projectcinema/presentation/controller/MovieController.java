package org.example.projectcinema.presentation.controller;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.exceptions.MovieNotFoundException;
import org.example.projectcinema.presentation.converter.MovieViewModelToMovieConverter;
import org.example.projectcinema.presentation.viewmodel.MovieViewModel;
import org.example.projectcinema.service.MovieService;
import org.example.projectcinema.service.collections.SessionHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
public class MovieController {

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
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        sessionHistoryService.addPageVisit(session,"All movies Page");
        return "movies";
    }

    @PostMapping("/movies/filter")
    public String getFilteredMovies(@RequestParam(value = "genre", required = false) String genreInput,
                                    @RequestParam(value = "rating", required = false) Double rating,
                                    Model model) {
        List<Movie> movies = movieService.getFilteredMovies(genreInput, rating);
        model.addAttribute("movies", movies);
        return "movies";
    }

    @GetMapping("/addmovie")
    public String showAddMovieForm(Model model, HttpSession session) {
        log.info("Displaying form to add a new movie");
        model.addAttribute("movieViewModel", new MovieViewModel());
        sessionHistoryService.addPageVisit(session,"Add movie Page");
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
        Movie movie = converter.convert(movieViewModel);
            movieService.addMovie(movie);
        return "redirect:/movies";
    }

    @GetMapping("/movie-details/{id}")
    public String viewMovieDetails(@PathVariable Long id, Model model, HttpSession session) {
        try {
            Movie movie = movieService.findByIdWithCinemas(id);
            model.addAttribute("movie", movie);
            sessionHistoryService.addPageVisit(session, "Movie details Page");
            return "movie-details";
        } catch (MovieNotFoundException ex) {
            log.error("Movie not found with id: {}", id);
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("condition", ex.getCondition());
            model.addAttribute("time",  ex.getTime());
            return "other-error";
        }
    }

    @GetMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteById(id);
        return "redirect:/movies";
    }

    @GetMapping("/title")
    public String findByTitleForm(@RequestParam(required = false) String title, Model model, HttpSession session) {
        Movie movies = movieService.findByTitle(title);
            model.addAttribute("movies", movies);
        sessionHistoryService.addPageVisit(session, "Find Movie by Title Page");
        return "movies-by-title";
    }

    @PostMapping("/title")
    public String findByTitle(@RequestParam String title, Model model, HttpSession session) {
        Movie movies = movieService.findByTitle(title);
        model.addAttribute("movies", movies);
        sessionHistoryService.addPageVisit(session, "Find Movie by Title Page");
        return "movies-by-title";
    }


    @GetMapping("/releaseDate")
    public String findByReleaseDateAfterForm(@RequestParam(required = false, defaultValue = "1919-01-01") LocalDate releaseDate, Model model, HttpSession session) {
        List<Movie> movies = movieService.findByReleaseDateAfter(releaseDate);
        model.addAttribute("movies", movies);
        sessionHistoryService.addPageVisit(session, "Find Movie by Release Date Page");
        return "movies-by-date";
    }

    @PostMapping("/releaseDate")
    public String findByReleaseDate(@RequestParam LocalDate releaseDate, Model model, HttpSession session) {
        List<Movie> movies = movieService.findByReleaseDateAfter(releaseDate);
        model.addAttribute("movies", movies);
        sessionHistoryService.addPageVisit(session, "Find Movie by Release Date Page");
        return "movies-by-date";
    }

}

