package org.example.projectcinema.repository;


import org.example.projectcinema.domain.Movie;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository {
    List<Movie> findAll();
    Movie save(Movie movie);
//    Movie findById(Long id);
//    void update(Movie movie);
    void deleteById(Long id);
    Movie findByIdWithCinemas(Long Id);
    Movie findByTitle(String title);
    List<Movie> findByReleaseDateAfter(LocalDate releaseDate);
}

