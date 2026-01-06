package org.example.projectcinema.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.repository.CinemaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Profile({"prod", "dev"})
@Transactional(readOnly = true)
public class CinemaJPARepository implements CinemaRepository {

    private static final Logger logger = LoggerFactory.getLogger(CinemaJPARepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Cinema save(Cinema cinema) {
        if (cinema.getId() == null) {
            entityManager.persist(cinema);
            logger.debug("Persisted new cinema: {}", cinema.getName());
            return cinema;
        } else {
            Cinema merged = entityManager.merge(cinema);
            logger.debug("Merged existing cinema: {}", merged.getName());
            return merged;
        }
    }

    public Cinema findById(Long id) {
        return entityManager.find(Cinema.class, id);
    }

    @Override
    public List<Cinema> findAll() {
        return entityManager.createQuery("SELECT c FROM Cinema c", Cinema.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Cinema cinema = findById(id);
        if (cinema != null) {
            entityManager.remove(cinema);
            logger.debug("Deleted cinema with id: {}", id);
        } else {
            logger.warn("Cannot delete cinema - not found with id: {}", id);
        }
    }

    @Override
    public Cinema findByIdWithMovies(Long id) {
        logger.debug("Fetching cinema with id {} including relationships", id);

        String movieQuery = "SELECT DISTINCT c FROM Cinema c " +
                "LEFT JOIN FETCH c.movies " +
                "WHERE c.id = :id";

        List<Cinema> results = entityManager.createQuery(movieQuery, Cinema.class)
                .setParameter("id", id)
                .getResultList();

        if (results.isEmpty()) {
            logger.warn("No cinema found with id: {}", id);
            return null;
        }

        Cinema cinema = results.get(0);

        String screenQuery = "SELECT DISTINCT c FROM Cinema c " +
                "LEFT JOIN FETCH c.screens " +
                "WHERE c.id = :id";

        entityManager.createQuery(screenQuery, Cinema.class)
                .setParameter("id", id)
                .getSingleResult();

        logger.debug("Fetched cinema '{}' with {} movies and {} screens",
                cinema.getName(),
                cinema.getMovies() != null ? cinema.getMovies().size() : 0,
                cinema.getScreens() != null ? cinema.getScreens().size() : 0);

        return cinema;
    }

    @Override
    public Cinema findByName(String name) {
        String jpql = "SELECT c FROM Cinema c WHERE c.name = :name";

        List<Cinema> results = entityManager.createQuery(jpql, Cinema.class)
                .setParameter("name", name)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Cinema> findByAddress(String address) {
        String jpql = "SELECT c FROM Cinema c WHERE c.address = :address";
        return entityManager.createQuery(jpql, Cinema.class)
                .setParameter("address", address)
                .getResultList();
    }
}