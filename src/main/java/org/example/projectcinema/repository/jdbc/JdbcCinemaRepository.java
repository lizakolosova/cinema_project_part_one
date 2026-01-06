package org.example.projectcinema.repository.jdbc;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.CinemaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Profile("jdbc")
public class JdbcCinemaRepository implements CinemaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert cinemaInserter;
    private static final Logger logger = LoggerFactory.getLogger(JdbcCinemaRepository.class);

    public JdbcCinemaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.cinemaInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("CINEMA")
                .usingGeneratedKeyColumns("CINEMA_ID");
    }

    private final RowMapper<Cinema> cinemaRowMapper = (rs, rowNum) -> new Cinema(
            rs.getLong("CINEMA_ID"),
            rs.getString("NAME"),
            rs.getString("ADDRESS"),
            rs.getInt("CAPACITY"),
            rs.getString("IMAGE")
    );

    @Override
    public List<Cinema> findAll() {
        logger.debug("Fetching all cinemas from the database");
        List<Cinema> cinemas = jdbcTemplate.query("SELECT * FROM CINEMA", cinemaRowMapper);
        logger.debug("Fetched {} cinemas", cinemas.size());
        return cinemas;
    }

    @Override
    @Transactional
    public Cinema save(Cinema cinema) {
        logger.debug("Attempting to save cinema: {}", cinema.getName());

        if (cinema.getId() != null) {
            String updateSql = """
                UPDATE CINEMA 
                SET NAME = ?, ADDRESS = ?, CAPACITY = ?, IMAGE = ? 
                WHERE CINEMA_ID = ?
                """;

            int updated = jdbcTemplate.update(
                    updateSql,
                    cinema.getName(),
                    cinema.getAddress(),
                    cinema.getCapacity(),
                    cinema.getImage(),
                    cinema.getId()
            );

            if (updated > 0) {
                logger.info("Cinema '{}' updated successfully", cinema.getName());
                return cinema;
            } else {
                logger.warn("No cinema found with ID {} to update", cinema.getId());
            }
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", cinema.getName());
        parameters.put("ADDRESS", cinema.getAddress());
        parameters.put("CAPACITY", cinema.getCapacity());
        parameters.put("IMAGE", cinema.getImage());

        Long generatedId = cinemaInserter.executeAndReturnKey(parameters).longValue();
        cinema.setId(generatedId);
        logger.info("Cinema '{}' saved with generated ID: {}", cinema.getName(), generatedId);

        return cinema;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Deleting cinema with ID: {}", id);

        jdbcTemplate.update(
                "DELETE FROM CINEMA_SCREEN_MOVIE WHERE SCREEN_ID IN " +
                        "(SELECT SCREEN_ID FROM CINEMA_SCREEN WHERE CINEMA_ID_FK = ?)",
                id
        );
        jdbcTemplate.update("DELETE FROM CINEMA_SCREEN WHERE CINEMA_ID_FK = ?", id);
        jdbcTemplate.update("DELETE FROM CINEMA_MOVIE WHERE CINEMA_ID = ?", id);
        jdbcTemplate.update("DELETE FROM CINEMA WHERE CINEMA_ID = ?", id);

        logger.info("Cinema with ID {} deleted successfully", id);
    }

    @Override
    @Transactional
    public Cinema findByIdWithMovies(Long id) {
        logger.debug("Fetching cinema with ID {} and its relationships", id);

        String movieSql = """
            SELECT c.CINEMA_ID, c.NAME, c.ADDRESS, c.CAPACITY, c.IMAGE,
                   m.MOVIE_ID, m.TITLE, m.RELEASE_DATE, m.RATING, m.GENRE, m.IMAGE as MOVIE_IMAGE
            FROM CINEMA c
            LEFT JOIN CINEMA_MOVIE cm ON c.CINEMA_ID = cm.CINEMA_ID
            LEFT JOIN MOVIE m ON cm.MOVIE_ID = m.MOVIE_ID
            WHERE c.CINEMA_ID = ?
            """;

        Cinema cinema = jdbcTemplate.query(movieSql, rs -> {
            Cinema result = null;

            while (rs.next()) {
                if (result == null) {
                    result = new Cinema(
                            rs.getLong("CINEMA_ID"),
                            rs.getString("NAME"),
                            rs.getString("ADDRESS"),
                            rs.getInt("CAPACITY"),
                            rs.getString("IMAGE")
                    );
                    result.setMovies(new ArrayList<>());
                }

                long movieId = rs.getLong("MOVIE_ID");
                if (!rs.wasNull()) {
                    Movie movie = new Movie(
                            movieId,
                            rs.getString("TITLE"),
                            rs.getDate("RELEASE_DATE").toLocalDate(),
                            rs.getDouble("RATING"),
                            Genre.valueOf(rs.getString("GENRE")),
                            rs.getString("MOVIE_IMAGE")
                    );
                    result.getMovies().add(movie);
                }
            }

            return result;
        }, id);

        if (cinema != null) {
            String screenSql = """
                SELECT SCREEN_ID, SCREEN_NUMBER, SCREENTYPE, SIZE
                FROM CINEMA_SCREEN
                WHERE CINEMA_ID_FK = ?
                """;

            List<CinemaScreen> screens = jdbcTemplate.query(screenSql, (rs, rowNum) -> {
                CinemaScreen screen = new CinemaScreen(
                        rs.getInt("SCREEN_NUMBER"),
                        cinema,
                        rs.getString("SCREENTYPE"),
                        rs.getInt("SIZE")
                );
                screen.setId(rs.getLong("SCREEN_ID"));
                return screen;
            }, id);

            cinema.setScreens(screens);

            logger.info("Fetched cinema '{}' with {} movies and {} screens",
                    cinema.getName(), cinema.getMovies().size(), screens.size());
        } else {
            logger.warn("No cinema found with ID {}", id);
        }

        return cinema;
    }

    @Override
    public Cinema findByName(String name) {
        logger.debug("Fetching cinema with name: {}", name);

        try {
            Cinema cinema = jdbcTemplate.queryForObject(
                    "SELECT * FROM CINEMA WHERE NAME = ?",
                    cinemaRowMapper,
                    name
            );
            logger.info("Found cinema with name: {}", name);
            return cinema;

        } catch (EmptyResultDataAccessException e) {
            logger.warn("No cinema found with name: {}", name);
            return null;
        }
    }

    @Override
    public List<Cinema> findByAddress(String address) {
        logger.debug("Fetching cinemas with address: {}", address);

        List<Cinema> cinemas = jdbcTemplate.query(
                "SELECT * FROM CINEMA WHERE ADDRESS = ?",
                cinemaRowMapper,
                address
        );

        logger.debug("Found {} cinemas with address: {}", cinemas.size(), address);
        return cinemas;
    }
}