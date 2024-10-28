package org.example.projectcinema.service;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.repository.CinemaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository cinemaRepository;
    private static final Logger logger = LoggerFactory.getLogger(CinemaServiceImpl.class);

    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public List<Cinema> getAllCinemas() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        logger.debug("Retrieved {} cinemas from the repository.", cinemas.size());
        return cinemas;
    }

    @Override
    public List<Cinema> getCinemasByCapacity(int minCapacity) {
        return cinemaRepository.findAll().stream()
                .filter(cinema -> cinema.getCapacity() >= minCapacity) // Ensure getCapacity() is defined in your Cinema class
                .collect(Collectors.toList());
    }

    public void saveCinema(Cinema cinema) {
        logger.debug("Saving cinema: {}", cinema.getName()); // Ensure getName() is defined in your Cinema class
        cinemaRepository.save(cinema);
    }

    public void addCinema(Cinema cinema) {
        cinemaRepository.save(cinema);
    }

    public Cinema findByName(String name) {
        String trimmedName = name.trim(); // Trim whitespace
        logger.debug("Searching for cinema with name: {}", trimmedName);

        List<Cinema> cinemas = cinemaRepository.findAll(); // Fetch the current cinemas from the repository
        logger.debug("Current cinemas in the repository: {}", cinemas.stream().map(Cinema::getName).collect(Collectors.toList()));

        return cinemas.stream()
                .filter(cinema -> {
                    boolean matches = cinema.getName().equalsIgnoreCase(trimmedName); // Use equalsIgnoreCase for case-insensitive comparison
                    logger.debug("Comparing with cinema: {}. Matches: {}", cinema.getName(), matches);
                    return matches;
                })
                .findFirst()
                .orElse(null);
    }
}