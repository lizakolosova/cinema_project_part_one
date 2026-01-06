package org.example.projectcinema;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.CinemaRepository;
import org.example.projectcinema.repository.CinemaScreenRepository;
import org.example.projectcinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@Profile({"prod", "dev", "JPA"})
public class JPARunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(JPARunner.class);

    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final CinemaScreenRepository cinemaScreenRepository;

    public JPARunner(
            CinemaRepository cinemaRepository,
            MovieRepository movieRepository,
            CinemaScreenRepository cinemaScreenRepository) {
        this.cinemaRepository = cinemaRepository;
        this.movieRepository = movieRepository;
        this.cinemaScreenRepository = cinemaScreenRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // ✅ Check if data already exists
        if (!cinemaRepository.findAll().isEmpty()) {
            logger.info("Data already exists in database, skipping seed");
            return;
        }

        logger.info("Starting data seeding for JPA profile...");
        seedData();
        logger.info("Data seeding completed successfully for JPA profile");
    }

    private void seedData() {
        // ✅ STEP 1: Create and save movies first (they have no dependencies)
        Movie movie1 = new Movie("Monsters, Inc.", LocalDate.of(2001, 11, 2), 8.1, Genre.ANIMATION, "movie1.jpg");
        Movie movie2 = new Movie("Toy Story 4", LocalDate.of(2019, 6, 21), 7.8, Genre.ANIMATION, "movie2.jpg");
        Movie movie3 = new Movie("The Lion King", LocalDate.of(2019, 7, 19), 6.8, Genre.ADVENTURE, "movie3.jpg");
        Movie movie4 = new Movie("Wall-E", LocalDate.of(2008, 6, 27), 8.0, Genre.ANIMATION, "movie4.jpg");

        movie1 = movieRepository.save(movie1);
        movie2 = movieRepository.save(movie2);
        movie3 = movieRepository.save(movie3);
        movie4 = movieRepository.save(movie4);

        logger.info("Created {} movies", 4);

        // ✅ STEP 2: Create and save cinemas (they have no dependencies)
        Cinema cinema1 = new Cinema("Cinema 1", "127 Kattenstraat", 200, "cinema1.jpg");
        Cinema cinema2 = new Cinema("Cinema 2", "365 Pothoekstraat", 150, "cinema2.jpg");
        Cinema cinema3 = new Cinema("Cinema 3", "398 Predikerinnenstraat", 300, "cinema3.jpg");
        Cinema cinema4 = new Cinema("Cinema 4", "741 Bredabaan", 220, "cinema4.jpg");

        cinema1 = cinemaRepository.save(cinema1);
        cinema2 = cinemaRepository.save(cinema2);
        cinema3 = cinemaRepository.save(cinema3);
        cinema4 = cinemaRepository.save(cinema4);

        logger.info("Created {} cinemas", 4);

        // ✅ STEP 3: Create screens (linked to cinemas)
        CinemaScreen screen1 = new CinemaScreen(1, cinema1, "IMAX", 100);
        CinemaScreen screen2 = new CinemaScreen(2, cinema2, "Regular", 50);
        CinemaScreen screen3 = new CinemaScreen(1, cinema3, "Regular", 75);
        CinemaScreen screen4 = new CinemaScreen(2, cinema4, "Small", 50);

        // Add screens to cinemas (bidirectional relationship)
        cinema1.addScreen(screen1);
        cinema2.addScreen(screen2);
        cinema3.addScreen(screen3);
        cinema4.addScreen(screen4);

        // ✅ IMPORTANT: Save screens BEFORE using them in movie relationships
        screen1 = cinemaScreenRepository.save(screen1);
        screen2 = cinemaScreenRepository.save(screen2);
        screen3 = cinemaScreenRepository.save(screen3);
        screen4 = cinemaScreenRepository.save(screen4);

        logger.info("Created {} cinema screens", 4);

        movie1.addScreen(screen1);
        movie2.addScreen(screen1);
        movie3.addScreen(screen2);
        movie4.addScreen(screen3);
        movie4.addScreen(screen4);

        logger.info("Linked movies to screens");

        movie1.addCinema(cinema1);
        movie1.addCinema(cinema2);

        movie2.addCinema(cinema2);
        movie2.addCinema(cinema3);

        movie3.addCinema(cinema3);
        movie3.addCinema(cinema4);

        movie4.addCinema(cinema4);

        logger.info("Linked movies to cinemas");

        cinemaRepository.save(cinema1);
        cinemaRepository.save(cinema2);
        cinemaRepository.save(cinema3);
        cinemaRepository.save(cinema4);

        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);
        movieRepository.save(movie4);

        logger.info("All relationships persisted successfully");
    }
}