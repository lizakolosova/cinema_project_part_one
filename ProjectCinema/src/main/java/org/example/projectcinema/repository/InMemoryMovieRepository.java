package org.example.projectcinema.repository;


import org.example.projectcinema.domain.Movie;

import java.util.ArrayList;
import java.util.List;

public class InMemoryMovieRepository implements MovieRepository {
    private List<Movie> movies = new ArrayList<>();

    @Override
    public List<Movie> findAll() {
        return movies;
    }

    @Override
    public void save(Movie movie) {
        movies.add(movie);
    }
}
