package org.example.projectcinema.repository;

import org.example.projectcinema.domain.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InMemoryMovieRepository implements MovieRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryMovieRepository.class);

    private final List<Movie> movies = new ArrayList<>();

    @Override
    public List<Movie> findAll() {
        logger.info("Fetching all movies. Total number of movies: {}", movies.size());
        return movies;
    }

    @Override
    public void save(Movie movie) {
        logger.info("Attempting to save movie: {}", movie != null ? movie.getTitle() : "null");

        if (movie == null) {
            logger.error("Movie is null. Cannot save.");
            throw new IllegalArgumentException("Movie cannot be null");
        }

        for (Movie existingMovie : movies) {
            if (existingMovie.getTitle() != null && existingMovie.getTitle().equals(movie.getTitle())) {
                logger.warn("Movie with title '{}' already exists. Skipping save.", movie.getTitle());
                return;
            }
        }

        movies.add(movie);
        logger.info("Movie '{}' has been successfully saved. Total number of movies: {}", movie.getTitle(), movies.size());
    }
}