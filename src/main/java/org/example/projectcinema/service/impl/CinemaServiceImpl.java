package org.example.projectcinema.service.impl;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.CinemaDTO;
import org.example.projectcinema.dto.mapper.CinemaMapper;
import org.example.projectcinema.exceptions.CinemaNotFoundException;
import org.example.projectcinema.repository.CinemaRepository;
import org.example.projectcinema.service.CinemaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CinemaServiceImpl implements CinemaService {

    private static final Logger logger = LoggerFactory.getLogger(CinemaServiceImpl.class);
    private final CinemaRepository cinemaRepository;

    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public List<CinemaDTO> getAllCinemas() {
        logger.debug("Fetching all cinemas");
        List<Cinema> cinemas = cinemaRepository.findAll();
        logger.debug("Retrieved {} cinemas", cinemas.size());
        return CinemaMapper.toDTOList(cinemas);
    }

    @Override
    public List<CinemaDTO> getCinemasByCapacity(int minCapacity) {
        logger.debug("Fetching cinemas with minimum capacity: {}", minCapacity);

        if (minCapacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }

        List<Cinema> cinemas = cinemaRepository.findAll().stream()
                .filter(cinema -> cinema.getCapacity() >= minCapacity)
                .collect(Collectors.toList());

        logger.debug("Found {} cinemas meeting capacity requirement", cinemas.size());
        return CinemaMapper.toDTOList(cinemas);
    }

    @Override
    @Transactional
    public CinemaDTO save(Cinema cinema) {
        logger.debug("Saving cinema: {}", cinema.getName());

        Cinema saved = cinemaRepository.save(cinema);
        logger.info("Cinema '{}' saved with id: {}", saved.getName(), saved.getId());

        return CinemaMapper.toDTO(saved);
    }

    @Override
    public CinemaDTO findById(Long id) {
        logger.debug("Fetching cinema with id: {}", id);
        Cinema cinema = cinemaRepository.findByIdWithMovies(id);

        if (cinema == null) {
            logger.warn("Cinema with id '{}' not found", id);
            throw new CinemaNotFoundException("Cinema not found with id: " + id);
        }

        logger.debug("Cinema with id '{}' retrieved successfully", id);
        return CinemaMapper.toDTO(cinema);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Deleting cinema with id: {}", id);

        Cinema cinema = cinemaRepository.findByIdWithMovies(id);
        if (cinema == null) {
            throw new CinemaNotFoundException("Cannot delete: Cinema not found with id: " + id);
        }

        for (Movie movie : new ArrayList<>(cinema.getMovies())) {
            cinema.removeMovie(movie);
        }

        for (CinemaScreen screen : new ArrayList<>(cinema.getScreens())) {
            for (Movie movie : new ArrayList<>(screen.getMovies())) {
                movie.getScreens().remove(screen);
            }
            screen.getMovies().clear();
            cinema.removeScreen(screen);
        }

        cinemaRepository.deleteById(id);
    }

    @Override
    public Optional<CinemaDTO> findByName(String name) {
        logger.debug("Fetching cinema with name: {}", name);
        Cinema cinema = cinemaRepository.findByName(name);
        return Optional.ofNullable(cinema)
                .map(CinemaMapper::toDTO);
    }

    @Override
    public List<CinemaDTO> findByAddress(String address) {
        logger.debug("Fetching cinemas at address: {}", address);
        List<Cinema> cinemas = cinemaRepository.findByAddress(address);
        logger.debug("Found {} cinemas at address", cinemas.size());
        return CinemaMapper.toDTOList(cinemas);
    }
}