package org.example.projectcinema.service;


import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.MovieDTO;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();
    List<Movie> getMoviesByGenreAndRating(Genre genre, Double rating);
    void addMovie(Movie movie);
    List<Movie> getFilteredMovies(String genreInput, Double rating);
    Movie findByIdWithCinemas(Long id);
    void  deleteById(Long id);
    Movie findByTitle(String title);
    List<Movie> findByReleaseDateAfter(LocalDate releaseDate);
    List<MovieDTO> getAllMoviesForJson();
}