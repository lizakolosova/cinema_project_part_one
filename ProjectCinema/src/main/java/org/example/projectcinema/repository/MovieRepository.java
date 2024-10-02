package org.example.projectcinema.repository;


import org.example.projectcinema.domain.Movie;

import java.util.List;

public interface MovieRepository {
    List<Movie> findAll();
    void save(Movie movie);
}

