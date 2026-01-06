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
                .withTableName("MOVIE")
                .usingGeneratedKeyColumns("MOVIE_ID");
    }

    private final RowMapper<Movie> movieRowMapper = (rs, rowNum) -> new Movie(
            rs.getLong("MOVIE_ID"),
            rs.getString("TITLE"),
            rs.getDate("RELEASE_DATE").toLocalDate(),
            rs.getDouble("RATING"),
            Genre.valueOf(rs.getString("GENRE")),
            rs.getString("IMAGE")
    );

    @Override
    public List<Movie> findAll() {
        logger.debug("Fetching all movies from the database");
        List<Movie> movies = jdbcTemplate.query("SELECT * FROM MOVIE", movieRowMapper);
        logger.debug("Fetched {} movies", movies.size());
        return movies;
    }

    @Override
    @Transactional
    public Movie save(Movie movie) {
        logger.debug("Attempting to save movie: {}", movie.getTitle());

        if (movie.getId() != null) {
            String updateSql = """
                UPDATE MOVIE 
                SET TITLE = ?, RELEASE_DATE = ?, RATING = ?, GENRE = ?, IMAGE = ? 
                WHERE MOVIE_ID = ?
                """;

            int updated = jdbcTemplate.update(
                    updateSql,
                    movie.getTitle(),
                    movie.getReleaseDate(),
                    movie.getRating(),
                    movie.getGenre().name(),
                    movie.getImage(),
                    movie.getId()
            );

            if (updated > 0) {
                logger.info("Movie '{}' updated successfully", movie.getTitle());
                return movie;
            }
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("TITLE", movie.getTitle());
        parameters.put("RELEASE_DATE", movie.getReleaseDate());
        parameters.put("RATING", movie.getRating());
        parameters.put("GENRE", movie.getGenre().name());
        parameters.put("IMAGE", movie.getImage());

        Long generatedId = movieInserter.executeAndReturnKey(parameters).longValue();
        movie.setId(generatedId);
        logger.info("Movie '{}' saved with ID {}", movie.getTitle(), generatedId);

        return movie;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Deleting movie with ID: {}", id);

        jdbcTemplate.update("DELETE FROM CINEMA_SCREEN_MOVIE WHERE MOVIE_ID = ?", id);
        jdbcTemplate.update("DELETE FROM CINEMA_MOVIE WHERE MOVIE_ID = ?", id);
        jdbcTemplate.update("DELETE FROM MOVIE WHERE MOVIE_ID = ?", id);

        logger.info("Movie with ID {} deleted successfully", id);
    }

    @Override
    @Transactional
    public Movie findByIdWithCinemas(Long id) {
        logger.debug("Fetching movie with ID {} and associated cinemas", id);

        String sql = """
            SELECT m.MOVIE_ID, m.TITLE, m.RELEASE_DATE, m.RATING, m.GENRE, m.IMAGE,
                   c.CINEMA_ID, c.NAME, c.ADDRESS, c.CAPACITY, c.IMAGE as CINEMA_IMAGE
            FROM MOVIE m
            LEFT JOIN CINEMA_MOVIE cm ON m.MOVIE_ID = cm.MOVIE_ID
            LEFT JOIN CINEMA c ON c.CINEMA_ID = cm.CINEMA_ID
            WHERE m.MOVIE_ID = ?
            """;

        Movie movie = jdbcTemplate.query(sql, rs -> {
            Movie result = null;

            while (rs.next()) {
                if (result == null) {
                    result = new Movie(
                            rs.getLong("MOVIE_ID"),
                            rs.getString("TITLE"),
                            rs.getDate("RELEASE_DATE").toLocalDate(),
                            rs.getDouble("RATING"),
                            Genre.valueOf(rs.getString("GENRE")),
                            rs.getString("IMAGE")
                    );
                    result.setCinemas(new ArrayList<>());
                }

                long cinemaId = rs.getLong("CINEMA_ID");
                if (!rs.wasNull()) {
                    Cinema cinema = new Cinema(
                            cinemaId,
                            rs.getString("NAME"),
                            rs.getString("ADDRESS"),
                            rs.getInt("CAPACITY"),
                            rs.getString("CINEMA_IMAGE")
                    );
                    result.getCinemas().add(cinema);
                }
            }

            if (result != null) {
                logger.info("Fetched movie '{}' with {} cinemas",
                        result.getTitle(), result.getCinemas().size());
            } else {
                logger.warn("No movie found with ID {}", id);
            }

            return result;
        }, id);

        return movie;
    }

    @Override
    public Movie findByTitle(String title) {
        logger.debug("Fetching movie with title: {}", title);

        try {
            Movie movie = jdbcTemplate.queryForObject(
                    "SELECT * FROM MOVIE WHERE TITLE = ?",
                    movieRowMapper,
                    title
            );
            logger.info("Found movie with title: {}", title);
            return movie;

        } catch (EmptyResultDataAccessException e) {
            logger.warn("No movie found with title: {}", title);
            return null;
        }
    }

    @Override
    public List<Movie> findByReleaseDateAfter(LocalDate releaseDate) {
        logger.debug("Fetching movies released after: {}", releaseDate);

        List<Movie> movies = jdbcTemplate.query(
                "SELECT * FROM MOVIE WHERE RELEASE_DATE > ?",
                movieRowMapper,
                releaseDate
        );

        logger.debug("Found {} movies released after {}", movies.size(), releaseDate);
        return movies;
    }
}