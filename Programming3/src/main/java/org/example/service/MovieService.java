package org.example.service;

import org.example.domain.Movie;

import java.util.List;


public interface MovieService {
    List<Movie> getAllMovies();
    List<Movie> getMoviesByGenreAndRating(String genreInput, Double rating);
}