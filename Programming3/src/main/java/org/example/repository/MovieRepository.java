package org.example.repository;

import org.example.domain.Movie;

import java.util.List;

public interface MovieRepository {
    List<Movie> findAll();
    void save(Movie movie);
}

