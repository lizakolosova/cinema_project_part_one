package org.example;

import org.example.presentation.CinemaConsolePresenter;
import org.example.presentation.CinemaConsoleView;
import org.example.repository.InMemoryCinemaRepository;
import org.example.repository.InMemoryMovieRepository;
import org.example.service.CinemaServiceImpl;
import org.example.service.MovieServiceImpl;

public class CinemaApplication {
    public static void main(String[] args) {

        InMemoryCinemaRepository cinemaRepository = new InMemoryCinemaRepository();
        InMemoryMovieRepository movieRepository = new InMemoryMovieRepository();

        CinemaServiceImpl cinemaService = new CinemaServiceImpl(cinemaRepository);
        MovieServiceImpl movieService = new MovieServiceImpl(movieRepository);

        CinemaConsoleView view = new CinemaConsoleView();
        CinemaConsolePresenter presenter = new CinemaConsolePresenter(cinemaService, movieService, view);

        DataFactory.seedData(cinemaRepository, movieRepository);

        presenter.run();
    }
}