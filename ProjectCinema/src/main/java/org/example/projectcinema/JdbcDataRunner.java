package org.example.projectcinema;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.CinemaRepository;
import org.example.projectcinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@Profile("jdbc")
public class JdbcDataRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(JdbcDataRunner.class);

    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final JdbcTemplate jdbcTemplate;

    public JdbcDataRunner(
            CinemaRepository cinemaRepository,
            MovieRepository movieRepository,
            JdbcTemplate jdbcTemplate) {
        this.cinemaRepository = cinemaRepository;
        this.movieRepository = movieRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM CINEMA", Integer.class);
        if (count != null && count > 0) {
            logger.info("Data already exists in database, skipping seed");
            return;
        }

        logger.info("Starting data seeding for JDBC profile...");
        seedData();
        logger.info("Data seeding completed successfully for JDBC profile");
    }

    private void seedData() {
        Movie movie1 = new Movie("Monsters, Inc.", LocalDate.of(2001, 11, 2), 8.1, Genre.ANIMATION, "movie1.jpg");
        Movie movie2 = new Movie("Toy Story 4", LocalDate.of(2019, 6, 21), 7.8, Genre.ANIMATION, "movie2.jpg");
        Movie movie3 = new Movie("The Lion King", LocalDate.of(2019, 7, 19), 6.8, Genre.ADVENTURE, "movie3.jpg");
        Movie movie4 = new Movie("Wall-E", LocalDate.of(2008, 6, 27), 8.0, Genre.ANIMATION, "movie4.jpg");

        movie1 = movieRepository.save(movie1);
        movie2 = movieRepository.save(movie2);
        movie3 = movieRepository.save(movie3);
        movie4 = movieRepository.save(movie4);

        logger.info("Created {} movies", 4);

        Cinema cinema1 = new Cinema("Cinema 1", "127 Kattenstraat", 200, "cinema1.jpg");
        Cinema cinema2 = new Cinema("Cinema 2", "365 Pothoekstraat", 150, "cinema2.jpg");
        Cinema cinema3 = new Cinema("Cinema 3", "398 Predikerinnenstraat", 300, "cinema3.jpg");
        Cinema cinema4 = new Cinema("Cinema 4", "741 Bredabaan", 220, "cinema4.jpg");

        cinema1 = cinemaRepository.save(cinema1);
        cinema2 = cinemaRepository.save(cinema2);
        cinema3 = cinemaRepository.save(cinema3);
        cinema4 = cinemaRepository.save(cinema4);

        logger.info("Created {} cinemas", 4);

        createScreen(1, cinema1.getId(), "IMAX", 100);
        createScreen(2, cinema2.getId(), "Regular", 50);
        createScreen(1, cinema3.getId(), "Regular", 75);
        createScreen(2, cinema4.getId(), "Small", 50);

        logger.info("Created {} cinema screens", 4);

        Long screen1Id = getScreenId(1, cinema1.getId());
        Long screen2Id = getScreenId(2, cinema2.getId());
        Long screen3Id = getScreenId(1, cinema3.getId());
        Long screen4Id = getScreenId(2, cinema4.getId());

        linkMovieToScreen(screen1Id, movie1.getId());
        linkMovieToScreen(screen1Id, movie2.getId());
        linkMovieToScreen(screen2Id, movie3.getId());
        linkMovieToScreen(screen3Id, movie4.getId());
        linkMovieToScreen(screen4Id, movie4.getId());

        logger.info("Linked movies to screens");

        linkMovieToCinema(cinema1.getId(), movie1.getId());
        linkMovieToCinema(cinema2.getId(), movie1.getId());

        linkMovieToCinema(cinema2.getId(), movie2.getId());
        linkMovieToCinema(cinema3.getId(), movie2.getId());

        linkMovieToCinema(cinema3.getId(), movie3.getId());
        linkMovieToCinema(cinema4.getId(), movie3.getId());

        linkMovieToCinema(cinema4.getId(), movie4.getId());

        logger.info("Linked movies to cinemas");
        logger.info("All relationships persisted successfully");
    }

    private void createScreen(int screenNumber, Long cinemaId, String screenType, int size) {
        jdbcTemplate.update(
                "INSERT INTO CINEMA_SCREEN (SCREEN_NUMBER, CINEMA_ID_FK, SCREENTYPE, SIZE) VALUES (?, ?, ?, ?)",
                screenNumber, cinemaId, screenType, size
        );
    }

    private Long getScreenId(int screenNumber, Long cinemaId) {
        return jdbcTemplate.queryForObject(
                "SELECT SCREEN_ID FROM CINEMA_SCREEN WHERE SCREEN_NUMBER = ? AND CINEMA_ID_FK = ?",
                Long.class,
                screenNumber, cinemaId
        );
    }

    private void linkMovieToScreen(Long screenId, Long movieId) {
        jdbcTemplate.update(
                "INSERT INTO CINEMA_SCREEN_MOVIE (SCREEN_ID, MOVIE_ID) VALUES (?, ?)",
                screenId, movieId
        );
    }

    private void linkMovieToCinema(Long cinemaId, Long movieId) {
        jdbcTemplate.update(
                "INSERT INTO CINEMA_MOVIE (CINEMA_ID, MOVIE_ID) VALUES (?, ?)",
                cinemaId, movieId
        );
    }
}
