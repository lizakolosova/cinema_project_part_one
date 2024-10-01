package org.example.service;


import org.example.domain.Genre;
import org.example.domain.Movie;
import org.example.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

    public class MovieServiceImpl implements MovieService {
        private final MovieRepository movieRepository;

        public MovieServiceImpl(MovieRepository movieRepository) {
            this.movieRepository = movieRepository;
        }

        @Override
        public List<Movie> getAllMovies() {
            return movieRepository.findAll();
        }

        @Override
        public List<Movie> getMoviesByGenreAndRating(String genreInput, Double rating) {
            Genre genre;
            try {
                genre = Genre.valueOf(genreInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid genre provided: " + genreInput);
            }

            List<Movie> filteredMovies = new ArrayList<>();

            for (Movie movie : movieRepository.findAll()) {
                boolean matchesGenre = movie.getGenre() == genre;
                boolean matchesRating = rating == null || movie.getRating() >= rating;

                if (matchesGenre && matchesRating) {
                    filteredMovies.add(movie);
                }
            }

            return filteredMovies;
        }
    }
