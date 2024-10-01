package org.example;

import org.example.domain.Cinema;
import org.example.domain.CinemaScreen;
import org.example.domain.Genre;
import org.example.domain.Movie;
import org.example.repository.CinemaRepository;
import org.example.repository.MovieRepository;

import java.time.LocalDate;

public class DataFactory {
    public static void seedData(CinemaRepository cinemaRepository, MovieRepository movieRepository) {
        Cinema cinema1 = new Cinema("Cinema 1", "127 Kattenstraat", 200);
        Cinema cinema2 = new Cinema("Cinema 2", "365 Pothoekstraat", 150);
        Cinema cinema3 = new Cinema("Cinema 3", "398 Predikerinnenstraat", 300);
        Cinema cinema4 = new Cinema("Cinema 4", "741 Bredabaan", 220);

        cinemaRepository.save(cinema1);
        cinemaRepository.save(cinema2);
        cinemaRepository.save(cinema3);

        CinemaScreen screen1 = new CinemaScreen(1, cinema1, "IMAX", 100);
        CinemaScreen screen2 = new CinemaScreen(2, cinema1, "Regular", 50);
        CinemaScreen screen3 = new CinemaScreen(1, cinema2, "Regular", 75);
        CinemaScreen screen4 = new CinemaScreen(2, cinema2, "Small", 50);

        cinema1.addScreen(screen1);
        cinema1.addScreen(screen2);
        cinema2.addScreen(screen3);
        cinema2.addScreen(screen4);

        Movie movie1 = new Movie("Avengers: Endgame", LocalDate.of(2019, 4, 26), 8.5, Genre.ACTION);
        Movie movie2 = new Movie("Toy Story 4", LocalDate.of(2019, 6, 21), 7.8, Genre.ANIMATION);
        Movie movie3 = new Movie("The Lion King", LocalDate.of(2019, 7, 19), 6.8, Genre.ADVENTURE);
        Movie movie4 = new Movie("Joker", LocalDate.of(2019, 10, 4), 8.4, Genre.DRAMA);

        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);
        movieRepository.save(movie4);

        screen1.addMovie(movie1);
        screen1.addMovie(movie2);
        screen2.addMovie(movie3);
        screen3.addMovie(movie4);
    }
}
