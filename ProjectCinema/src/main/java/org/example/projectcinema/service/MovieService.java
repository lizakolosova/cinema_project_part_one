package org.example.projectcinema.service;


import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();
    public List<Movie> getMoviesByGenreAndRating(Genre genre, Double rating);
    void addMovie(Movie movie);
    public Movie findByTitle(String title);
}