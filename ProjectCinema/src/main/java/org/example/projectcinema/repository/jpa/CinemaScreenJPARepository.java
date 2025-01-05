package org.example.projectcinema.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.projectcinema.domain.CinemaScreen;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"prod","dev"})
@Transactional
public class CinemaScreenJPARepository {

    @PersistenceContext
    private EntityManager entityManager;

    public CinemaScreen save(CinemaScreen cinemaScreen) {
            entityManager.merge(cinemaScreen);
        return cinemaScreen;
    }

    public CinemaScreen findExistingCinemaScreen(CinemaScreen cinemaScreen) {
        return entityManager.createQuery(
                        "SELECT cs FROM CinemaScreen cs WHERE cs.cinema.id = :cinemaId AND cs.screenNumber = :screenNumber",
                        CinemaScreen.class)
                .setParameter("cinemaId", cinemaScreen.getCinema().getId())
                .setParameter("screenNumber", cinemaScreen.getScreenNumber())
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
