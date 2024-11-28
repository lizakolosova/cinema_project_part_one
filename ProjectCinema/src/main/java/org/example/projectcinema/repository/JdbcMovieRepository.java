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
            rs.getInt("movie_id"),
            rs.getString("title"),
            rs.getDate("release_date").toLocalDate(),
            rs.getDouble("rating"),
            Genre.valueOf(rs.getString("genre")),
            rs.getString("image")
    );

    @Override
    public List<Movie> findAll() {
        return jdbcTemplate.query("SELECT * FROM movie", movieRowMapper);
    }

    @Override
    public Movie findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM movie WHERE movie_id=?",
                movieRowMapper, id);
    }

    @Override
    public Movie save(Movie movie) {
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

        int generatedId = movieInserter.executeAndReturnKey(parameters).intValue();
        movie.setId(generatedId);
        return movie;
    }

    @Override
    public void update(Movie movie) {
        jdbcTemplate.update("UPDATE movie SET title=?, release_date=?, rating=?, genre=?, image=? WHERE movie_id=?",
                movie.getTitle(), movie.getReleaseDate(), movie.getRating(), movie.getGenre(), movie.getImage());
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM cinema_screen_movie WHERE screen_id IN (SELECT screen_id FROM cinema_screen WHERE movie_id_fk=?)", id);
        jdbcTemplate.update("DELETE FROM cinema_screen_movie WHERE movie_id = ?", id);
        jdbcTemplate.update("DELETE FROM cinema_screen WHERE movie_id_fk = ?", id);
        jdbcTemplate.update("DELETE FROM cinema_movie WHERE movie_id = ?", id);
        jdbcTemplate.update("DELETE FROM movie WHERE movie_id = ?", id);
    }



    @Transactional
    @Override
    public Movie findByIdWithCinemas(int Id) {
        String sql = """
        SELECT m.*, c.cinema_id, c.name, c.address, c.capacity, c.image
                        FROM movie m
                        LEFT JOIN cinema_movie cm ON cm.movie_id = m.movie_id
                        LEFT JOIN cinema c ON c.cinema_id = cm.cinema_id
                        WHERE m.movie_id = ?
                            
    """;

        return jdbcTemplate.query(sql, rs -> {
            Movie movie = null;

            while (rs.next()) {
                if (movie == null) {
                    movie = new Movie(
                            rs.getInt("movie_id"),
                            rs.getString("title"),
                            rs.getDate("release_date").toLocalDate(),
                            rs.getDouble("rating"),
                            Genre.valueOf(rs.getString("genre")),
                            rs.getString("image")
                    );
                    movie.setCinemas(new ArrayList<>());
                }
                int cinemaId = rs.getInt("cinema_id");
                if (!rs.wasNull()) {
                    Cinema cinema = new Cinema(
                            rs.getInt("cinema_id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getInt("capacity"),
                            rs.getString("image")
                    );
                    movie.getCinemas().add(cinema);
                }
            }

            return movie;
        }, Id);
    }
}


