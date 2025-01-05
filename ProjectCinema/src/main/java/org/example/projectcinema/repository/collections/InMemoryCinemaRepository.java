package org.example.projectcinema.repository.collections;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.repository.CinemaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("collections")
public class InMemoryCinemaRepository implements CinemaRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryCinemaRepository.class);

    private final List<Cinema> cinemas = new ArrayList<>();

    @Override
    public List<Cinema> findAll() {
        logger.info("Fetching all cinemas. Current number of cinemas: {}", cinemas.size());
        return new ArrayList<>(cinemas);
    }

    @Override
    public Cinema save(Cinema cinema) {
        logger.info("Attempting to save cinema: {}", cinema != null ? cinema.getName() : "null");

        if (cinema == null || cinema.getName() == null) {
            logger.error("Cinema or cinema name is null. Cannot save.");
            throw new IllegalArgumentException("Cinema or cinema name cannot be null");
        }
        cinemas.add(cinema);
        logger.info("Cinema '{}' has been saved successfully. Total number of cinemas: {}", cinema.getName(), cinemas.size());
        return cinema;
    }
    @Override
    public void deleteById(Long id) {
        boolean removed = cinemas.removeIf(cinema -> cinema.getId() == id);
        if (removed) {
            logger.info("Cinema with ID {} removed successfully.", id);
        } else {
            logger.info("Cinema with ID {} not found.", id);
        }
    }
    @Override
    public Cinema findByIdWithMovies(Long id) {
        logger.debug("Fetching cinema with ID {} with movies.", id);
        return cinemas.stream()
                .filter(cinema -> cinema.getId() == id)
                .findFirst()
                .orElse(null);
    }
    @Override
    public Cinema findByName(String name) {
        logger.debug("Fetching cinema by name {}.", name);
        return cinemas.stream()
                .filter(cinema -> cinema.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Cinema> findByAddress(String address) {
        logger.debug("Fetching cinema by address {}.", address);
        return cinemas.stream()
                .filter(cinema -> cinema.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

}

