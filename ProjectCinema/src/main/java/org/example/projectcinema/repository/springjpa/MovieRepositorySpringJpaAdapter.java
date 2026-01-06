package org.example.projectcinema.repository.springjpa;

import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Profile("JPA")
public class MovieRepositorySpringJpaAdapter implements MovieRepository {

    private static final Logger logger = LoggerFactory.getLogger(MovieRepositorySpringJpaAdapter.class);

    private final MovieSpringDataJpaRepository movieRepository;

    public MovieRepositorySpringJpaAdapter(MovieSpringDataJpaRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> findAll() {
        logger.debug("Fetching all movies using Spring Data JPA");
        List<Movie> movies = movieRepository.findAll();
        logger.debug("Retrieved {} movies", movies.size());
        return movies;
    }

    @Override
    public Movie save(Movie movie) {
        logger.debug("Saving movie: {}", movie.getTitle());
        Movie saved = movieRepository.save(movie);
        logger.info("Movie '{}' saved with id: {}", saved.getTitle(), saved.getId());
        return saved;
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("Deleting movie with id: {}", id);
        movieRepository.deleteById(id);
        logger.info("Movie with id '{}' deleted", id);
    }

    @Override
    public Movie findByIdWithCinemas(Long id) {
        Movie movie = movieRepository.findByIdWithCinemas(id);
        if (movie != null) {
            movie = movieRepository.findByIdWithScreens(id);
        }
        return movie;
    }

    @Override
    public Movie findByTitle(String title) {
        logger.debug("Fetching movie by title: {}", title);
        return movieRepository.findByTitle(title).orElse(null);
    }

    @Override
    public List<Movie> findByReleaseDateAfter(LocalDate releaseDate) {
        logger.debug("Fetching movies released after: {}", releaseDate);
        List<Movie> movies = movieRepository.findByReleaseDateAfter(releaseDate);
        logger.debug("Found {} movies released after {}", movies.size(), releaseDate);
        return movies;
    }
}