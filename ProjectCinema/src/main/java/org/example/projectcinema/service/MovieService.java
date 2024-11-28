package org.example.projectcinema.service;


import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();
    List<Movie> getMoviesByGenreAndRating(Genre genre, Double rating);
    void addMovie(Movie movie);
    List<Movie> getFilteredMovies(String genreInput, Double rating);
    Movie findByIdWithCinemas(int id);
    void  deleteById(int id);
}