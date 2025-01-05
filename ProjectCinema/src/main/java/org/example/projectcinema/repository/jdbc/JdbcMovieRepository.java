package org.example.projectcinema.repository.jdbc;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Profile("jdbc")
public class JdbcMovieRepository implements MovieRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert movieInserter;

    private static final Logger logger = LoggerFactory.getLogger(JdbcMovieRepository.class);

    public JdbcMovieRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.movieInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("movie")
                .usingGeneratedKeyColumns("movie_id");
    }

    private final RowMapper<Movie> movieRowMapper = (rs, rowNum) -> new Movie(
            rs.getLong("movie_id"),
            rs.getString("title"),
            rs.getDate("release_date").toLocalDate(),
            rs.getDouble("rating"),
            Genre.valueOf(rs.getString("genre")),
            rs.getString("image")
    );

    @Override
    public List<Movie> findAll() {
        logger.info("Fetching all movies from the database.");
        List<Movie> movies = jdbcTemplate.query("SELECT * FROM movie", movieRowMapper);
        logger.info("Fetched {} movies.", movies.size());
        return movies;
    }

    @Override
    public Movie save(Movie movie) {
        logger.info("Attempting to save movie with title '{}'.", movie.getTitle());

        String checkSql = "SELECT COUNT(*) FROM movie WHERE title = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, movie.getTitle());

        if (count > 0) {
            logger.info("Movie with title '{}' already exists, skipping insert.", movie.getTitle());
            return movie;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", movie.getTitle());
        parameters.put("release_date", movie.getReleaseDate());
        parameters.put("rating", movie.getRating());
        parameters.put("genre", movie.getGenre());
        parameters.put("image", movie.getImage());

        Long generatedId = movieInserter.executeAndReturnKey(parameters).longValue();
        movie.setId(generatedId);
        logger.info("Movie with title '{}' saved with ID {}.", movie.getTitle(), generatedId);
        return movie;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.info("Attempting to delete movie with ID {}.", id);
        jdbcTemplate.update("DELETE FROM cinema_screen_movie WHERE screen_id IN (SELECT screen_id FROM cinema_screen WHERE movie_id_fk=?)", id);
        jdbcTemplate.update("DELETE FROM cinema_screen_movie WHERE movie_id = ?", id);
        jdbcTemplate.update("DELETE FROM cinema_screen WHERE movie_id_fk = ?", id);
        jdbcTemplate.update("DELETE FROM cinema_movie WHERE movie_id = ?", id);
        jdbcTemplate.update("DELETE FROM movie WHERE movie_id = ?", id);
        logger.info("Movie with ID {} and related data deleted.", id);
    }

    @Transactional
    @Override
    public Movie findByIdWithCinemas(Long Id) {
        logger.info("Fetching movie with ID {} and associated cinemas.", Id);

        String sql = """
        SELECT m.*, c.cinema_id, c.name, c.address, c.capacity, c.image
        FROM movie m
        LEFT JOIN cinema_movie cm ON cm.movie_id = m.movie_id
        LEFT JOIN cinema c ON c.cinema_id = cm.cinema_id
        WHERE m.movie_id = ?
        """;

        Movie movie = jdbcTemplate.query(sql, rs -> {
            Movie resultMovie = null;

            while (rs.next()) {
                if (resultMovie == null) {
                    resultMovie = new Movie(
                            rs.getLong("movie_id"),
                            rs.getString("title"),
                            rs.getDate("release_date").toLocalDate(),
                            rs.getDouble("rating"),
                            Genre.valueOf(rs.getString("genre")),
                            rs.getString("image")
                    );
                    resultMovie.setCinemas(new ArrayList<>());
                }
                int cinemaId = rs.getInt("cinema_id");
                if (!rs.wasNull()) {
                    Cinema cinema = new Cinema(
                            rs.getLong("cinema_id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getInt("capacity"),
                            rs.getString("image")
                    );
                    resultMovie.getCinemas().add(cinema);
                }
            }

            return resultMovie;
        }, Id);

        if (movie != null) {
            logger.info("Fetched movie with ID {} and {} associated cinemas.", Id, movie.getCinemas().size());
        } else {
            logger.warn("No movie found with ID {}.", Id);
        }

        return movie;
    }

    @Override
    public Movie findByTitle(String title) {
        logger.info("Fetching movie with title '{}'.", title);

        String sql = "SELECT * FROM movie WHERE title = ?";
        try {
            Movie movie = jdbcTemplate.queryForObject(sql, movieRowMapper, title);
            logger.info("Found movie with title '{}'.", title);
            return movie;
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No movie found with title '{}'.", title);
            return null;
        }
    }

    @Override
    public List<Movie> findByReleaseDateAfter(LocalDate releaseDate) {
        logger.info("Fetching movies released after '{}'.", releaseDate);

        String sql = "SELECT * FROM movie WHERE release_date > ?";
        List<Movie> movies = jdbcTemplate.query(sql, movieRowMapper, releaseDate);

        logger.info("Found {} movies released after '{}'.", movies.size(), releaseDate);
        return movies;
    }
}



