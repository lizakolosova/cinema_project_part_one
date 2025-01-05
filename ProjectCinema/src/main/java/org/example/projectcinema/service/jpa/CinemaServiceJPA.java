package org.example.projectcinema.service.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.CinemaDTO;
import org.example.projectcinema.dto.CinemaScreenDTO;
import org.example.projectcinema.dto.MovieDTO;
import org.example.projectcinema.exceptions.CinemaNotFoundException;
import org.example.projectcinema.repository.jpa.CinemaJPARepository;
import org.example.projectcinema.service.CinemaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile({"prod", "dev"})
public class CinemaServiceJPA implements CinemaService {

    private final CinemaJPARepository cinemaRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private static final Logger logger = LoggerFactory.getLogger(CinemaServiceJPA.class);

    @Autowired
    public CinemaServiceJPA(CinemaJPARepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public List<Cinema> getAllCinemas() {
        logger.info("Fetching all cinemas from repository.");
        List<Cinema> cinemas = cinemaRepository.findAll();
        if (cinemas.isEmpty()) {
            logger.warn("No cinemas found in the repository.");
            throw new CinemaNotFoundException("Cinema not found.", 404, "Not found");
        }
        logger.debug("Retrieved {} cinemas from the repository.", cinemas.size());
        return cinemas;
    }

    @Override
    public List<Cinema> getCinemasByCapacity(int minCapacity) {
        logger.info("Fetching cinemas with minimum capacity of {}.", minCapacity);
        List<Cinema> cinemas = cinemaRepository.findAll().stream()
                .filter(cinema -> cinema.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
        logger.debug("Found {} cinemas with the specified capacity.", cinemas.size());
        return cinemas;
    }

    @Override
    @Transactional
    public void saveCinema(Cinema cinema) {
        logger.info("Saving cinema: {}", cinema.getName());
        cinemaRepository.save(cinema);
        logger.debug("Cinema '{}' saved successfully.", cinema.getName());
    }

    @Override
    public Cinema findByIdWithMovies(Long id) {
        logger.info("Fetching cinema by id: {} with associated movies.", id);
        Cinema cinema = cinemaRepository.findByIdWithMovies(id);
        if (cinema == null) {
            logger.warn("Cinema with id {} not found.", id);
        }
        return cinema;
    }

    @Override
    @Transactional
    public void deleteById(Long cinemaId) {
        logger.info("Attempting to delete cinema with id: {}", cinemaId);
        Cinema cinema = cinemaRepository.findById(cinemaId);
        if (cinema == null) {
            logger.warn("Cinema with id {} not found for deletion.", cinemaId);
            throw new CinemaNotFoundException("Cinema not found. Id:" + cinemaId, 404, "Not found");
        } else {
            logger.debug("Cinema found. Removing associated movies and screens.");
            for (CinemaScreen screen : cinema.getScreens()) {
                for (Movie movie : screen.getMovies()) {
                    movie.getScreens().remove(screen);
                    entityManager.merge(movie);
                    logger.debug("Removed screen from movie '{}'.", movie.getTitle());
                }
            }
            for (Movie movie : cinema.getMovies()) {
                movie.getCinemas().remove(cinema);
                entityManager.merge(movie);
                logger.debug("Removed cinema from movie '{}'.", movie.getTitle());
            }

            cinemaRepository.deleteById(cinemaId);
            logger.info("Cinema with id {} deleted successfully.", cinemaId);
        }
    }

    @Override
    public Cinema findByName(String name) {
        logger.info("Fetching cinema by name: {}", name);
        Cinema cinema = cinemaRepository.findByName(name);
        if (cinema == null) {
            logger.warn("Cinema with name '{}' not found.", name);
        }
        return cinema;
    }

    @Override
    public List<Cinema> findCinemasByAddress(String address) {
        logger.info("Fetching cinemas by address: {}", address);
        List<Cinema> cinemas = cinemaRepository.findByAddress(address);
        logger.debug("Found {} cinemas at the address '{}'.", cinemas.size(), address);
        return cinemas;
    }

    @Override
    public List<CinemaDTO> getAllCinemasForJson() {
        logger.info("Fetching all cinemas for JSON conversion.");
        List<Cinema> cinemas = cinemaRepository.findAll();

        List<CinemaDTO> cinemaDTOs = new ArrayList<>();
        for (Cinema cinema : cinemas) {
            logger.debug("Converting cinema '{}' to DTO.", cinema.getName());
            List<CinemaScreenDTO> screens = cinema.getScreens().stream()
                    .map(screen -> new CinemaScreenDTO(screen.getId(), screen.getScreenNumber(), screen.getScreenType(), screen.getSize()))
                    .collect(Collectors.toList());

            List<MovieDTO> movies = cinema.getMovies().stream()
                    .map(movie -> new MovieDTO(movie.getId(), movie.getTitle(), movie.getReleaseDate(), movie.getRating(), movie.getGenre().toString(), movie.getImage()))
                    .collect(Collectors.toList());

            CinemaDTO cinemaDTO = new CinemaDTO(cinema.getId(), cinema.getName(), cinema.getAddress(), cinema.getCapacity(), cinema.getImage(), screens, movies);
            cinemaDTOs.add(cinemaDTO);
        }

        logger.debug("Converted {} cinemas to DTO format.", cinemaDTOs.size());
        return cinemaDTOs;
    }
}
