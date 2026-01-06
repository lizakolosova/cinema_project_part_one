package org.example.projectcinema.repository.springjpa;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.repository.CinemaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("JPA")
public class CinemaRepositorySpringJpaAdapter implements CinemaRepository {

    private static final Logger logger = LoggerFactory.getLogger(CinemaRepositorySpringJpaAdapter.class);

    private final CinemaSpringDataJpaRepository cinemaRepository;

    public CinemaRepositorySpringJpaAdapter(CinemaSpringDataJpaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public List<Cinema> findAll() {
        logger.debug("Fetching all cinemas using Spring Data JPA");
        List<Cinema> cinemas = cinemaRepository.findAll();
        logger.debug("Retrieved {} cinemas", cinemas.size());
        return cinemas;
    }

    @Override
    public Cinema save(Cinema cinema) {
        logger.debug("Saving cinema: {}", cinema.getName());
        Cinema saved = cinemaRepository.save(cinema);
        logger.info("Cinema '{}' saved with id: {}", saved.getName(), saved.getId());
        return saved;
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("Deleting cinema with id: {}", id);
        cinemaRepository.deleteById(id);
        logger.info("Cinema with id '{}' deleted", id);
    }

    @Override
    public Cinema findByIdWithMovies(Long id) {
        logger.debug("Fetching cinema with id {} including movies", id);
        Cinema cinema = cinemaRepository.findByIdWithMovies(id);
        if (cinema != null) {
            logger.debug("Retrieved cinema '{}' with {} movies and {} screens",
                    cinema.getName(),
                    cinema.getMovies().size(),
                    cinema.getScreens().size());
        } else {
            logger.warn("No cinema found with id: {}", id);
        }
        cinemaRepository.findByIdWithScreens(id);
        return cinema;
    }

    @Override
    public Cinema findByName(String name) {
        logger.debug("Fetching cinema by name: {}", name);
        return cinemaRepository.findByName(name).orElse(null);
    }

    @Override
    public List<Cinema> findByAddress(String address) {
        logger.debug("Fetching cinemas by address: {}", address);
        List<Cinema> cinemas = cinemaRepository.findByAddress(address);
        logger.debug("Found {} cinemas at address '{}'", cinemas.size(), address);
        return cinemas;
    }
}