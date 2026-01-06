package org.example.projectcinema.repository;

import org.example.projectcinema.domain.Movie;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository {
    List<Movie> findAll();
    Movie save(Movie movie);
    void deleteById(Long id);
    Movie findByIdWithCinemas(Long Id);
    Movie findByTitle(String title);
    List<Movie> findByReleaseDateAfter(LocalDate releaseDate);
}

