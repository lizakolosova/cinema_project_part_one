package org.example.projectcinema.service.collections;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.dto.CinemaDTO;
import org.example.projectcinema.dto.CinemaScreenDTO;
import org.example.projectcinema.dto.MovieDTO;
import org.example.projectcinema.exceptions.CinemaNotFoundException;
import org.example.projectcinema.repository.CinemaRepository;
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
@Profile({"collections", "jdbc"})
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository cinemaRepository;
    private static final Logger logger = LoggerFactory.getLogger(CinemaServiceImpl.class);

    @Autowired
    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public List<Cinema> getAllCinemas() {
        logger.debug("Fetching all cinemas from repository...");
        List<Cinema> cinemas = cinemaRepository.findAll();
        if (cinemas.isEmpty()) {
            logger.warn("No cinemas found.");
            throw new CinemaNotFoundException("Cinema not found. Id:", 404, "Not found");
        }
        logger.debug("Retrieved {} cinemas from the repository.", cinemas.size());
        return cinemas;
    }

    @Override
    public List<Cinema> getCinemasByCapacity(int minCapacity) {
        logger.debug("Fetching cinemas with a minimum capacity of {}.", minCapacity);
        List<Cinema> filteredCinemas = cinemaRepository.findAll().stream()
                .filter(cinema -> cinema.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
        logger.debug("Found {} cinemas with the specified capacity.", filteredCinemas.size());
        return filteredCinemas;
    }

    @Override
    public void saveCinema(Cinema cinema) {
        logger.debug("Saving cinema: {}", cinema.getName());
        cinemaRepository.save(cinema);
        logger.info("Cinema '{}' saved successfully.", cinema.getName());
    }

    @Override
    public Cinema findByIdWithMovies(Long id) {
        logger.debug("Fetching cinema with id: {}", id);
        Cinema cinema = cinemaRepository.findByIdWithMovies(id);
        if (cinema == null) {
            logger.warn("Cinema with id '{}' not found.", id);
            throw new CinemaNotFoundException("Cinema not found. Id:", 404, "Not found");
        }
        logger.debug("Cinema with id '{}' retrieved successfully.", id);
        return cinema;
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("Deleting cinema with id: {}", id);
        cinemaRepository.deleteById(id);
        logger.info("Cinema with id '{}' deleted successfully.", id);
    }

    @Override
    public Cinema findByName(String name) {
        logger.debug("Fetching cinema with name: {}", name);
        Cinema cinema = cinemaRepository.findByName(name);
        if (cinema == null) {
            logger.warn("Cinema with name '{}' not found.", name);
        }
        return cinema;
    }

    @Override
    public List<Cinema> findCinemasByAddress(String address) {
        logger.debug("Fetching cinemas located at address: {}", address);
        List<Cinema> cinemas = cinemaRepository.findByAddress(address);
        logger.debug("Found {} cinemas at the specified address.", cinemas.size());
        return cinemas;
    }

    @Override
    public List<CinemaDTO> getAllCinemasForJson() {
        logger.debug("Converting all cinemas to DTO format...");
        List<Cinema> cinemas = cinemaRepository.findAll();
        List<CinemaDTO> cinemaDTOs = new ArrayList<>();

        for (Cinema cinema : cinemas) {
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
