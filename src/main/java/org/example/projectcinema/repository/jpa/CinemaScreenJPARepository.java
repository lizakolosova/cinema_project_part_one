package org.example.projectcinema.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.repository.CinemaScreenRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Profile({"prod", "dev"})
@Transactional(readOnly = true)
public class CinemaScreenJPARepository implements CinemaScreenRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CinemaScreen save(CinemaScreen cinemaScreen) {
        if (cinemaScreen.getId() == null) {
            entityManager.persist(cinemaScreen);
            return cinemaScreen;
        } else {
            return entityManager.merge(cinemaScreen);
        }
    }
}