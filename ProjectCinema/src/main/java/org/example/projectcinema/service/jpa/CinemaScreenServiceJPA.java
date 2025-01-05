package org.example.projectcinema.service.jpa;

import jakarta.transaction.Transactional;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.repository.jpa.CinemaJPARepository;
import org.example.projectcinema.repository.jpa.CinemaScreenJPARepository;
import org.example.projectcinema.service.CinemaScreenService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Profile({"prod", "dev"})
public class CinemaScreenServiceJPA implements CinemaScreenService {

    private static final Logger logger = LoggerFactory.getLogger(CinemaScreenServiceJPA.class);

    private final CinemaScreenJPARepository cinemaScreenRepository;
    private final CinemaJPARepository cinemaRepository;

    public CinemaScreenServiceJPA(CinemaScreenJPARepository cinemaScreenRepository, CinemaJPARepository cinemaRepository) {
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
        Cinema associatedCinema = cinemaRepository.findById(cinemaScreen.getCinema().getId());
        CinemaScreen existingScreen = cinemaScreenRepository.findExistingCinemaScreen(cinemaScreen);
        if (existingScreen != null) {
            logger.info("CinemaScreen already exists. Returning the existing CinemaScreen with ID {}", existingScreen.getId());
            return existingScreen;
        }
        cinemaScreen.setCinema(associatedCinema);
        return cinemaScreenRepository.save(cinemaScreen);
    }
}


