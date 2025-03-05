package org.example.projectcinema;

import lombok.extern.slf4j.Slf4j;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.CinemaRepository;
import org.example.projectcinema.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("collections")
@Slf4j
public class DataFactory implements CommandLineRunner {

    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public DataFactory(CinemaRepository cinemaRepository, MovieRepository movieRepository) {
        this.cinemaRepository = cinemaRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) {
        seedData();
    }

    public void seedData() {
        Cinema cinema1 = new Cinema("Cinema 1", "127 Kattenstraat", 200, "cinema1.jpg");
        Cinema cinema2 = new Cinema("Cinema 2", "365 Pothoekstraat", 150, "cinema2.jpg");
        Cinema cinema3 = new Cinema("Cinema 3", "398 Predikerinnenstraat", 300, "cinema3.jpg");
        Cinema cinema4 = new Cinema("Cinema 4", "741 Bredabaan", 220, "cinema4.jpg");

        cinemaRepository.save(cinema1);
        cinemaRepository.save(cinema2);
        cinemaRepository.save(cinema3);
        cinemaRepository.save(cinema4);

        CinemaScreen screen1 = new CinemaScreen(1, cinema1, "IMAX", 100);
        CinemaScreen screen2 = new CinemaScreen(2, cinema2,  "Regular", 50);
        CinemaScreen screen3 = new CinemaScreen(3, cinema3, "Regular", 75);
        CinemaScreen screen4 = new CinemaScreen(2,cinema4, "Small", 50);

        Movie movie1 = new Movie("Monsters, Inc.", LocalDate.of(2001, 11, 2), 8.1, Genre.ANIMATION, "movie1.jpg");
        Movie movie2 = new Movie("Toy Story 4", LocalDate.of(2019, 6, 21), 7.8, Genre.ANIMATION, "movie2.jpg");
        Movie movie3 = new Movie("The Lion King", LocalDate.of(2019, 7, 19), 6.8, Genre.ADVENTURE, "movie3.jpg");
        Movie movie4 = new Movie("Wall-E", LocalDate.of(2008, 6, 27), 8.0, Genre.ANIMATION, "movie4.jpg");

        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);
        movieRepository.save(movie4);

        screen1.addMovie(movie1);
        screen1.addMovie(movie2);
        screen2.addMovie(movie3);
        screen3.addMovie(movie4);
        screen4.addMovie(movie4);

        movie1.addCinema(cinema1);
        movie1.addCinema(cinema2);
        cinema1.addMovie(movie1);
        cinema2.addMovie(movie1);

        movie2.addCinema(cinema2);
        movie2.addCinema(cinema3);
        cinema2.addMovie(movie2);
        cinema3.addMovie(movie2);

        movie3.addCinema(cinema3);
        movie3.addCinema(cinema4);
        cinema3.addMovie(movie3);
        cinema4.addMovie(movie3);

        movie4.addCinema(cinema4);
        cinema4.addMovie(movie4);

        log.info("Data seeding completed successfully");
    }
}