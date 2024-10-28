package org.example.projectcinema.repository;

import org.example.projectcinema.domain.Cinema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCinemaRepository implements CinemaRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryCinemaRepository.class);

    private final List<Cinema> cinemas = new ArrayList<>();

    @Override
    public List<Cinema> findAll() {
        logger.info("Fetching all cinemas. Current number of cinemas: {}", cinemas.size());
        return new ArrayList<>(cinemas);
    }

    @Override
    public void save(Cinema cinema) {
        logger.info("Attempting to save cinema: {}", cinema != null ? cinema.getName() : "null");

        if (cinema == null || cinema.getName() == null) {
            logger.error("Cinema or cinema name is null. Cannot save.");
            throw new IllegalArgumentException("Cinema or cinema name cannot be null");
        }

        for (Cinema existingCinema : cinemas) {
            if (existingCinema.getName().equals(cinema.getName())) {
                logger.warn("Cinema with the name '{}' already exists. Skipping save.", cinema.getName());
                return;
            }
        }

        cinemas.add(cinema);
        logger.info("Cinema '{}' has been saved successfully. Total number of cinemas: {}", cinema.getName(), cinemas.size());
    }
}
