package org.example.projectcinema.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Profile({"prod", "dev"})
@Transactional(readOnly = true)
public class MovieJPARepository implements MovieRepository {

    private static final Logger logger = LoggerFactory.getLogger(MovieJPARepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Movie save(Movie movie) {
        if (movie.getId() == null) {
            entityManager.persist(movie);
            logger.debug("Persisted new movie: {}", movie.getTitle());
            return movie;
        } else {
            Movie merged = entityManager.merge(movie);
            logger.debug("Merged existing movie: {}", merged.getTitle());
            return merged;
        }
    }

    public Movie findById(Long id) {
        return entityManager.find(Movie.class, id);
    }

    @Override
    public List<Movie> findAll() {
        return entityManager.createQuery("SELECT m FROM Movie m", Movie.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Movie movie = findById(id);
        if (movie != null) {
            entityManager.remove(movie);
            logger.debug("Deleted movie with id: {}", id);
        } else {
            logger.warn("Cannot delete movie - not found with id: {}", id);
        }
    }

    @Override
    public Movie findByIdWithCinemas(Long id) {
        logger.debug("Fetching movie with id {} including relationships", id);

        String cinemaQuery = "SELECT DISTINCT m FROM Movie m " +
                "LEFT JOIN FETCH m.cinemas " +
                "WHERE m.id = :id";

        List<Movie> results = entityManager.createQuery(cinemaQuery, Movie.class)
                .setParameter("id", id)
                .getResultList();

        if (results.isEmpty()) {
            logger.warn("No movie found with id: {}", id);
            return null;
        }

        Movie movie = results.get(0);

        String screenQuery = "SELECT DISTINCT m FROM Movie m " +
                "LEFT JOIN FETCH m.screens " +
                "WHERE m.id = :id";

        entityManager.createQuery(screenQuery, Movie.class)
                .setParameter("id", id)
                .getSingleResult();

        logger.debug("Fetched movie '{}' with {} cinemas and {} screens",
                movie.getTitle(),
                movie.getCinemas() != null ? movie.getCinemas().size() : 0,
                movie.getScreens() != null ? movie.getScreens().size() : 0);

        return movie;
    }

    @Override
    public Movie findByTitle(String title) {
        String jpql = "SELECT m FROM Movie m WHERE m.title = :title";

        List<Movie> results = entityManager.createQuery(jpql, Movie.class)
                .setParameter("title", title)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Movie> findByReleaseDateAfter(LocalDate releaseDate) {
        String jpql = "SELECT m FROM Movie m WHERE m.releaseDate > :releaseDate";
        return entityManager.createQuery(jpql, Movie.class)
                .setParameter("releaseDate", releaseDate)
                .getResultList();
    }
}