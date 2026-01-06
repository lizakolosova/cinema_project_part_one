package org.example.projectcinema.service;

import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.MovieDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<MovieDTO> getAllMovies();
    List<MovieDTO> getMoviesByGenreAndRating(Genre genre, Double rating);
    MovieDTO save(Movie movie);
    List<MovieDTO> getFilteredMovies(String genreInput, Double rating);
    MovieDTO findById(Long id);
    void deleteById(Long id);
    Optional<MovieDTO> findByTitle(String title);
    List<MovieDTO> findByReleaseDateAfter(LocalDate releaseDate);
}