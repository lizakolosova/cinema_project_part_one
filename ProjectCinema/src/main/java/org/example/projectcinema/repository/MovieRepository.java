package org.example.projectcinema.repository;


import org.example.projectcinema.domain.Movie;

import java.util.List;

public interface MovieRepository {
    List<Movie> findAll();
    Movie save(Movie movie);
    Movie findById(int id);
    void update(Movie movie);
    void deleteById(int id);
    Movie findByIdWithCinemas(int Id);
}

