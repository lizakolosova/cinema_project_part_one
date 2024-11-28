package org.example.projectcinema.repository;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
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
            rs.getInt("cinema_id"),
            rs.getString("name"),
            rs.getString("address"),
            rs.getInt("capacity"),
            rs.getString("image")
    );

    @Override
    public List<Cinema> findAll() {
        return jdbcTemplate.query("SELECT * FROM cinema", cinemaRowMapper);
    }

    @Override
    public Cinema findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM cinema WHERE cinema_id=?",
                cinemaRowMapper, id);
    }

    @Override
    public Cinema save(Cinema cinema) {
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

        int generatedId = cinemaInserter.executeAndReturnKey(parameters).intValue();
        cinema.setId(generatedId);
        return cinema;
    }

    @Override
    public void update(Cinema cinema) {
        jdbcTemplate.update("UPDATE cinema SET name=?, address=?, capacity=?, image=? WHERE cinema_id=?",
                cinema.getName(), cinema.getAddress(), cinema.getCapacity(), cinema.getImage(), cinema.getId());
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM cinema_screen_movie WHERE screen_id IN (SELECT screen_id FROM cinema_screen WHERE cinema_id_fk=?)", id);
        jdbcTemplate.update("DELETE FROM cinema_screen WHERE cinema_id_fk=?", id);
        jdbcTemplate.update("DELETE FROM cinema_movie WHERE cinema_id = ?", id);
        jdbcTemplate.update("DELETE FROM cinema WHERE cinema_id=?", id);
    }

    @Transactional
    @Override
    public Cinema findByIdWithMovies(int Id) {
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
                            rs.getInt("cinema_id"),
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
                            rs.getInt("movie_id"),
                            rs.getString("title"),
                            rs.getDate("release_date").toLocalDate(),
                            rs.getDouble("rating"),
                            Genre.valueOf(rs.getString("genre")),
                            rs.getString("image")
                    );
                    cinema.getMovies().add(movie);
                }
            }

            return cinema;
        }, Id);
    }
}



