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
                .filter(cinema -> cinema.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
    }

    @Override
    public void saveCinema(Cinema cinema) {
        logger.debug("Saving cinema: {}", cinema.getName());
        cinemaRepository.save(cinema);
    }

    @Override
    public Cinema findByIdWithMovies(int id) {
        return cinemaRepository.findByIdWithMovies(id);
    }
    @Override
    public void deleteById(int id) {
        cinemaRepository.deleteById(id);
    }
}