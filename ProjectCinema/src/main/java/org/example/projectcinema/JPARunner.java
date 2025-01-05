package org.example.projectcinema;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.service.CinemaScreenService;
import org.example.projectcinema.service.CinemaService;
import org.example.projectcinema.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@EnableTransactionManagement
@EnableJpaRepositories
@Profile({"prod", "dev", "JPA"})
public class JPARunner implements CommandLineRunner {
    private final CinemaService cinemaService;
    private final MovieService movieService;
    private final CinemaScreenService cinemaScreenService;
    private static final Logger logger = LoggerFactory.getLogger(JPARunner.class);

    @Autowired
    public JPARunner(CinemaService cinemaService, MovieService movieService, CinemaScreenService cinemaScreenService) {
        this.cinemaService = cinemaService;
        this.movieService = movieService;
        this.cinemaScreenService= cinemaScreenService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedData();
    }

    public void seedData() {
        logger.info("Seeding data using JPA...");

        Movie movie1 = new Movie("Avengers: Endgame", LocalDate.of(2019, 4, 26), 8.5, Genre.ACTION, "movie1.jpg");
        Movie movie2 = new Movie("Toy Story 4", LocalDate.of(2019, 6, 21), 7.8, Genre.ANIMATION, "movie2.jpg");
        Movie movie3 = new Movie("The Lion King", LocalDate.of(2019, 7, 19), 6.8, Genre.ADVENTURE, "movie3.jpg");
        Movie movie4 = new Movie("Joker", LocalDate.of(2019, 10, 4), 8.4, Genre.DRAMA, "movie4.jpg");

        Cinema cinema1 = new Cinema("Cinema 1", "127 Kattenstraat", 200, "cinema1.jpg");
        Cinema cinema2 = new Cinema("Cinema 2", "365 Pothoekstraat", 150, "cinema2.jpg");
        Cinema cinema3 = new Cinema("Cinema 3", "398 Predikerinnenstraat", 300, "cinema3.jpg");
        Cinema cinema4 = new Cinema("Cinema 4", "741 Bredabaan", 220, "cinema4.jpg");


        CinemaScreen screen1 = new CinemaScreen(1, "IMAX", 100);
        CinemaScreen screen2 = new CinemaScreen(2, "Regular", 50);
        CinemaScreen screen3 = new CinemaScreen(1, "Regular", 75);
        CinemaScreen screen4 = new CinemaScreen(2, "Small", 50);

        screen1.setCinema(cinema1);
        screen2.setCinema(cinema2);
        screen3.setCinema(cinema1);
        screen4.setCinema(cinema2);

        movieService.addMovie(movie1);
        movieService.addMovie(movie2);
        movieService.addMovie(movie3);
        movieService.addMovie(movie4);

        cinemaService.saveCinema(cinema1);
        cinemaService.saveCinema(cinema2);
        cinemaService.saveCinema(cinema3);
        cinemaService.saveCinema(cinema4);

        cinemaScreenService.saveCinemaScreen(screen1);
        cinemaScreenService.saveCinemaScreen(screen2);
        cinemaScreenService.saveCinemaScreen(screen3);
        cinemaScreenService.saveCinemaScreen(screen4);

        movie1.addScreen(screen1);
        screen1.addMovie(movie1);
        movie2.addScreen(screen1);
        screen1.addMovie(movie2);
        movie3.addScreen(screen2);
        screen2.addMovie(movie3);
        movie4.addScreen(screen3);
        screen3.addMovie(movie4);
        movie4.addScreen(screen4);
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

        logger.info("Data seeding completed successfully");
    }
}