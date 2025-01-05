package org.example.projectcinema.repository.jdbc;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.CinemaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Profile("jdbc")
public class JdbcCinemaRepository implements CinemaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert cinemaInserter;
    private static final Logger logger = LoggerFactory.getLogger(JdbcCinemaRepository.class);

    public JdbcCinemaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.cinemaInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cinema")
                .usingGeneratedKeyColumns("cinema_id");
    }

    private final RowMapper<Cinema> cinemaRowMapper = (rs, rowNum) -> new Cinema(
            rs.getLong("cinema_id"),
            rs.getString("name"),
            rs.getString("address"),
            rs.getInt("capacity"),
            rs.getString("image")
    );

    @Override
    public List<Cinema> findAll() {
        logger.debug("Fetching all cinemas from the database");
        List<Cinema> cinemas = jdbcTemplate.query("SELECT * FROM cinema", cinemaRowMapper);
        logger.debug("Fetched {} cinemas", cinemas.size());
        return cinemas;
    }

    @Override
    public Cinema save(Cinema cinema) {
        logger.debug("Attempting to save cinema: {}", cinema.getName());

        String checkSql = "SELECT COUNT(*) FROM cinema WHERE name = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, cinema.getName());

        if (count > 0) {
            logger.info("Cinema with name '{}' already exists, skipping insert.", cinema.getName());
            return cinema;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", cinema.getName());
        parameters.put("address", cinema.getAddress());
        parameters.put("capacity", cinema.getCapacity());
        parameters.put("image", cinema.getImage());

        Long generatedId = cinemaInserter.executeAndReturnKey(parameters).longValue();
        cinema.setId(generatedId);
        logger.info("Cinema '{}' saved with generated ID: {}", cinema.getName(), generatedId);
        return cinema;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Deleting cinema with ID: {}", id);
        jdbcTemplate.update("DELETE FROM cinema_screen_movie WHERE screen_id IN (SELECT screen_id FROM cinema_screen WHERE cinema_id_fk=?)", id);
        jdbcTemplate.update("DELETE FROM cinema_screen WHERE cinema_id_fk=?", id);
        jdbcTemplate.update("DELETE FROM cinema_movie WHERE cinema_id = ?", id);
        jdbcTemplate.update("DELETE FROM cinema WHERE cinema_id=?", id);
        logger.info("Cinema with ID: {} deleted successfully", id);
    }

    @Transactional
    @Override
    public Cinema findByIdWithMovies(Long Id) {
        logger.debug("Fetching cinema with ID: {} and its associated movies", Id);

        String sql = """
        SELECT c.*, m.movie_id, m.title, m.release_date, m.rating, m.genre, m.image
                             FROM cinema c
                             LEFT JOIN cinema_movie cm ON c.cinema_id = cm.cinema_id
                             LEFT JOIN movie m ON cm.movie_id = m.movie_id
                             WHERE c.cinema_id = ?
    """;

        return jdbcTemplate.query(sql, rs -> {
            Cinema cinema = null;

            while (rs.next()) {
                if (cinema == null) {
                    cinema = new Cinema(
                            rs.getLong("cinema_id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getInt("capacity"),
                            rs.getString("image")
                    );
                    cinema.setMovies(new ArrayList<>());
                }

                int movieId = rs.getInt("movie_id");
                if (!rs.wasNull()) {
                    Movie movie = new Movie(
                            rs.getLong("movie_id"),
                            rs.getString("title"),
                            rs.getDate("release_date").toLocalDate(),
                            rs.getDouble("rating"),
                            Genre.valueOf(rs.getString("genre")),
                            rs.getString("image")
                    );
                    cinema.getMovies().add(movie);
                    logger.debug("Added movie '{}' to cinema '{}'", movie.getTitle(), cinema.getName());
                }
            }

            if (cinema != null) {
                logger.info("Fetched cinema: {} with {} movies", cinema.getName(), cinema.getMovies().size());
            } else {
                logger.warn("No cinema found with ID: {}", Id);
            }

            return cinema;
        }, Id);
    }

    @Override
    public Cinema findByName(String name) {
        logger.debug("Fetching cinema with name: {}", name);
        Cinema cinema = jdbcTemplate.queryForObject(
                "SELECT * FROM cinema WHERE name = ?",
                cinemaRowMapper,
                name
        );
        if (cinema != null) {
            logger.info("Found cinema with name: {}", name);
        } else {
            logger.warn("No cinema found with name: {}", name);
        }
        return cinema;
    }

    @Override
    public List<Cinema> findByAddress(String address) {
        logger.debug("Fetching cinemas with address: {}", address);
        List<Cinema> cinemas = jdbcTemplate.query(
                "SELECT * FROM cinema WHERE address = ?",
                cinemaRowMapper,
                address
        );
        logger.debug("Found {} cinemas with address: {}", cinemas.size(), address);
        return cinemas;
    }
}




