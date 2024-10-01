package org.example.presentation;

import org.example.domain.Movie;
import org.example.service.CinemaService;
import org.example.service.MovieService;

import java.util.List;

public class CinemaConsolePresenter {
    private final CinemaService cinemaService;
    private final MovieService movieService;
    private final CinemaView view;

    public CinemaConsolePresenter(CinemaService cinemaService, MovieService movieService, CinemaView view) {
        this.cinemaService = cinemaService;
        this.movieService = movieService;
        this.view = view;
    }

    public void run() {
        boolean running = true;

        while (running) {
            view.showMenu();
            int choice = view.getUserChoice();

            switch (choice) {
                case 0:
                    running = false;
                    break;
                case 1:
                    view.showAllCinemas(cinemaService.getAllCinemas());
                    break;
                case 2:
                    int minCapacity = view.getMinCapacity();
                    view.showFilteredCinemas(cinemaService.getCinemasByCapacity(minCapacity));
                    break;
                case 3:
                    view.showAllMovies(movieService.getAllMovies());
                    break;
                case 4:
                    String genreInput = view.getGenreInput();
                    Double rating = view.getRatingInput();

                    try {
                        List<Movie> filteredMovies = movieService.getMoviesByGenreAndRating(genreInput, rating);
                        if (filteredMovies.isEmpty()) {
                            view.showMessage("No movies found for the selected genre and rating.");
                        } else {
                            view.showFilteredMovies(filteredMovies);
                        }
                    } catch (IllegalArgumentException e) {
                        view.showMessage(e.getMessage());
                    }
                    break;
                default:
                    view.showMessage("Invalid choice. Please try again.");
            }
        }
    }
}