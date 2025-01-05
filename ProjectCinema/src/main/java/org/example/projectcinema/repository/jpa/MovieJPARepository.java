package org.example.projectcinema.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.MovieRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Profile({"prod", "dev"})
@Repository
public class MovieJPARepository implements MovieRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Movie save(Movie movie) {
        Movie existingMovie = entityManager.createQuery(
                        "SELECT m FROM Movie m WHERE m.title = :title", Movie.class)
                .setParameter("title", movie.getTitle())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (existingMovie != null) {
            return existingMovie;
        }

        if (movie.getId() == null) {
            entityManager.persist(movie);
            return movie;
        } else {
            return entityManager.merge(movie);
        }
    }


    public Movie findById(Long id) {
        return entityManager.find(Movie.class, id);
    }

    @Override
    public List<Movie> findAll() {
        return entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
    }

    @Override
    public void deleteById(Long id) {
        Movie movie = findById(id);
        if (movie != null) {
            entityManager.remove(movie);
        }
    }

    @Override
    public Movie findByIdWithCinemas(Long id) {
        String jpql = "SELECT m FROM Movie m LEFT JOIN FETCH m.cinemas WHERE m.id = :id";
        List<Movie> result = entityManager.createQuery(jpql, Movie.class)
                .setParameter("id", id)
                .getResultList();
        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    public Movie findByTitle(String title) {
        String jpql = "SELECT m FROM Movie m LEFT JOIN FETCH m.cinemas WHERE m.title = :title";
        List<Movie> result = entityManager.createQuery(jpql, Movie.class)
                .setParameter("title", title)
                .getResultList();
        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    public List<Movie> findByReleaseDateAfter(LocalDate releaseDate) {
        String jpql = "SELECT m FROM Movie m LEFT JOIN FETCH m.cinemas WHERE m.releaseDate > :releaseDate";
        return entityManager.createQuery(jpql, Movie.class)
                .setParameter("releaseDate", releaseDate)
                .getResultList();
    }
}


