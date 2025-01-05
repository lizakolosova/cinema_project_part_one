package org.example.projectcinema.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.repository.CinemaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile({"prod", "dev"})
public class CinemaJPARepository implements CinemaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Cinema save(Cinema cinema) {
        Cinema existingCinema = entityManager.createQuery(
                        "SELECT c FROM Cinema c WHERE c.name = :name", Cinema.class)
                .setParameter("name", cinema.getName())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (existingCinema != null) {
            return existingCinema;
        }

        if (cinema.getId() == null) {
            entityManager.persist(cinema);
            return cinema;
        } else {
            return entityManager.merge(cinema);
        }
    }


    public Cinema findById(Long id) {
        return entityManager.find(Cinema.class, id);
    }

    @Override
    public List<Cinema> findAll() {
        return entityManager.createQuery("SELECT c FROM Cinema c", Cinema.class).getResultList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
            Cinema cinema = findById(id);
        if (cinema != null) {
            entityManager.remove(cinema);
        }
    }

    @Override
    public Cinema findByIdWithMovies(Long id) {
        String jpql = "SELECT c FROM Cinema c LEFT JOIN FETCH c.movies WHERE c.id = :id";
        return entityManager.createQuery(jpql, Cinema.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Cinema findByName(String name) {
        String jpql = "SELECT c FROM Cinema c WHERE c.name = :name";
        return entityManager.createQuery(jpql, Cinema.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public List<Cinema> findByAddress(String address) {
        String jpql = "SELECT c FROM Cinema c WHERE c.address = :address";
        return entityManager.createQuery(jpql, Cinema.class)
                .setParameter("address", address)
                .getResultList();
    }
}

