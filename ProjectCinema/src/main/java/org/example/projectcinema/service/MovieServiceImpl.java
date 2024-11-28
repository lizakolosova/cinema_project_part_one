package org.example.projectcinema.service;


import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.presentation.converters.StringToGenreConverter;
import org.example.projectcinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final StringToGenreConverter stringToGenreConverter;
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    public MovieServiceImpl(MovieRepository movieRepository, StringToGenreConverter stringToGenreConverter) {
        this.movieRepository = movieRepository;
        this.stringToGenreConverter = stringToGenreConverter;
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> getMoviesByGenreAndRating(Genre genre, Double rating) {
        return movieRepository.findAll().stream()
                .filter(movie -> (genre == null || movie.getGenre().equals(genre)))
                .filter(movie -> (rating == null || movie.getRating() >= rating))
                .collect(Collectors.toList());
    }

    @Override
    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    @Override
    public List<Movie> getFilteredMovies(String genreInput, Double rating) {
        Genre genre = null;
        if (genreInput != null && !genreInput.isEmpty()) {
            genre = stringToGenreConverter.convert(genreInput);
        }

        return getMoviesByGenreAndRating(genre, rating);
    }
    @Override
    public Movie findByIdWithCinemas(int id) {
        return movieRepository.findByIdWithCinemas(id);
    }
    @Override
    public void deleteById(int id) {
        movieRepository.deleteById(id);
    }
}