package org.example.projectcinema.service.springjpa;

import lombok.extern.slf4j.Slf4j;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.CinemaDTO;
import org.example.projectcinema.dto.CinemaScreenDTO;
import org.example.projectcinema.dto.MovieDTO;
import org.example.projectcinema.exceptions.CinemaNotFoundException;
import org.example.projectcinema.repository.springjpa.CinemaRepositorySpringJPA;
import org.example.projectcinema.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Profile("JPA")
public class CinemaServiceSpringJPA implements CinemaService {

    private final CinemaRepositorySpringJPA cinemaRepository;

    @Autowired
    public CinemaServiceSpringJPA(CinemaRepositorySpringJPA cinemaRepository) {
        this.cinemaRepository = cinemaRepository;

    }

    @Override
    public List<Cinema> getAllCinemas() {
            List<Cinema> cinemas = cinemaRepository.findAll();
            log.debug("Retrieved {} cinemas from the repository.", cinemas.size());
            return cinemas;
    }

    public Cinema findById(Long cinemaId) {
        return cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new CinemaNotFoundException("Cinema with ID " + cinemaId + " not found.", 404, "Cinema not found."));
    }

    @Override
    public void saveCinema(Cinema cinema) {
        log.debug("Attempting to save cinema: {}", cinema.getName());
            if (cinemaRepository.findByName(cinema.getName()).isPresent()) {
                log.warn("Cinema with the name '{}' already exists. Skipping save.", cinema.getName());
                return;
            }
            cinemaRepository.save(cinema);
            log.info("Cinema '{}' saved successfully.", cinema.getName());
    }


    @Override
    public void deleteById(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new CinemaNotFoundException("Cinema with ID " + cinemaId + " not found.", 404, "Cinema not found"));

        for (CinemaScreen screen : cinema.getScreens()) {
            for (Movie movie : screen.getMovies()) {
                movie.getScreens().remove(screen);
            }
        }

        for (Movie movie : cinema.getMovies()) {
            movie.getCinemas().remove(cinema);
        }
        cinemaRepository.deleteById(cinemaId);
        log.info("Cinema with ID {} deleted successfully.", cinemaId);
    }

    @Override
    public List<Cinema> getCinemasByCapacity(int minCapacity) {
            return cinemaRepository.findCinemaByCapacityAfter(minCapacity);
    }

    @Override
    public Cinema findByName(String name) {
        return cinemaRepository.findByName(name)
                .orElse(null);
    }

    @Override
    public List<Cinema> findCinemasByAddress(String address) {
        return cinemaRepository.findByAddress(address);
    }

    @Override
    public Cinema findByIdWithMovies(Long id) {
        Optional<Cinema> cinemaOptional = cinemaRepository.findById(id);
        if (cinemaOptional.isPresent()) {
            return cinemaOptional.get();
    } else {
            throw new CinemaNotFoundException("Cinema not found. Id:" + id, 404, "Not found");}
    }

    public List<CinemaDTO> getAllCinemasForJson() {
        List<Cinema> cinemas = cinemaRepository.findAll();

        List<CinemaDTO> cinemaDTOs = new ArrayList<>();
        for (Cinema cinema : cinemas) {
            List<CinemaScreenDTO> screens = cinema.getScreens().stream()
                    .map(screen -> new CinemaScreenDTO(screen.getId(),screen.getScreenNumber(), screen.getScreenType(), screen.getSize()))
                    .collect(Collectors.toList());

            List<MovieDTO> movies = cinema.getMovies().stream()
                    .map(movie -> new MovieDTO(movie.getId(), movie.getTitle(), movie.getReleaseDate(), movie.getRating(), movie.getGenre().toString(), movie.getImage()))
                    .collect(Collectors.toList());

            CinemaDTO cinemaDTO = new CinemaDTO(cinema.getId(), cinema.getName(), cinema.getAddress(), cinema.getCapacity(), cinema.getImage(), screens, movies);
            cinemaDTOs.add(cinemaDTO);
        }

        return cinemaDTOs;
    }
}


