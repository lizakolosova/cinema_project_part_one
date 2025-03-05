package org.example.projectcinema.service.springjpa;

import jakarta.transaction.Transactional;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.repository.springjpa.CinemaRepositorySpringJPA;
import org.example.projectcinema.repository.springjpa.CinemaScreenRepositorySpringJPA;
import org.example.projectcinema.service.CinemaScreenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("JPA")
@Service
public class CinemaScreenServiceSpringJPA implements CinemaScreenService {
    private static final Logger logger = LoggerFactory.getLogger(CinemaScreenServiceSpringJPA.class);

    private final CinemaScreenRepositorySpringJPA cinemaScreenRepository;
    private final CinemaRepositorySpringJPA cinemaRepository;

    public CinemaScreenServiceSpringJPA(CinemaScreenRepositorySpringJPA cinemaScreenRepository, CinemaRepositorySpringJPA cinemaRepository) {
        this.cinemaScreenRepository = cinemaScreenRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @Transactional
    @Override
    public CinemaScreen saveCinemaScreen(CinemaScreen cinemaScreen) {
        if (cinemaScreen == null || cinemaScreen.getCinema() == null) {
            throw new IllegalArgumentException("CinemaScreen or its associated Cinema must not be null.");
        }

        if (cinemaScreen.getCinema().getId() == null) {
            logger.warn("Cinema associated with cinema screen which ID is {} does not exist. Skipping creation of CinemaScreen.", cinemaScreen.getId());
            return null;
        }
        Cinema associatedCinema = cinemaRepository.findById(cinemaScreen.getCinema().getId()).get();
        CinemaScreen existingScreen = cinemaScreenRepository.findExistingCinemaScreen(cinemaScreen.getScreenNumber(),cinemaScreen.getCinema().getId());
        if (existingScreen != null) {
            logger.info("CinemaScreen already exists. Returning the existing CinemaScreen with ID {}", existingScreen.getId());
            return existingScreen;
        }
        cinemaScreen.setCinema(associatedCinema);
        return cinemaScreenRepository.save(cinemaScreen);
    }
}
