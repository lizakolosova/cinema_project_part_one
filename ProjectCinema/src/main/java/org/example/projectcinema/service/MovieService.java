package org.example.projectcinema.service;


import org.example.projectcinema.domain.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();
    List<Movie> getMoviesByGenreAndRating(String genreInput, Double rating);
}