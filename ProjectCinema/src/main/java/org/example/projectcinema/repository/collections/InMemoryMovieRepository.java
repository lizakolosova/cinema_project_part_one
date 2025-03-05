package org.example.projectcinema.repository.collections;

import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("collections")
public class InMemoryMovieRepository implements MovieRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryMovieRepository.class);

    private final List<Movie> movies = new ArrayList<>();
    private long currentId = 1;

    @Override
    public List<Movie> findAll() {
        logger.info("Fetching all movies. Total number of movies: {}", movies.size());
        return movies;
    }

    @Override
    public Movie save(Movie movie) {
        logger.info("Saving movie: {}", movie != null ? movie.getTitle() : "null");

        if (movie == null) {
            logger.error("Movie is null. Cannot save.");
            throw new IllegalArgumentException("Movie cannot be null");
        }

        if (movie.getId() == null) {
            movie.setId(currentId++);
        }

        movies.add(movie);
        logger.info("Movie '{}' has been successfully saved. Total number of movies: {}", movie.getTitle(), movies.size());
        return movie;
    }

    @Override
    public void deleteById(Long id) {
        boolean removed = movies.removeIf(movie -> movie.getId() == id);
        if (removed) {
            logger.info("Movie with id {} was successfully deleted.", id);
        } else {
            logger.warn("Movie with id {} not found. No deletion occurred.", id);
        }
    }
    @Override
    public Movie findByIdWithCinemas(Long id) {
        logger.debug("Fetching movie with ID {} with cinemas.", id);
        return movies.stream()
                .filter(movie -> movie.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Movie findByTitle(String title) {
        logger.debug("Fetching cinema by title {}.", title);
        return movies.stream()
                .filter(movie -> movie.getTitle() == title)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Movie> findByReleaseDateAfter(LocalDate releaseDate) {
        logger.debug("Fetching cinema from date {}.", releaseDate);
        return movies.stream()
                .filter(movie -> movie.getReleaseDate().isAfter(releaseDate))
                .collect(Collectors.toList());
    }
}

