package org.example.projectcinema.service.collections;

import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.MovieDTO;
import org.example.projectcinema.dto.mapper.MovieMapper;
import org.example.projectcinema.exceptions.MovieNotFoundException;
import org.example.projectcinema.presentation.converter.StringToGenreConverter;
import org.example.projectcinema.repository.MovieRepository;
import org.example.projectcinema.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile({"collections", "jdbc"})
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final StringToGenreConverter stringToGenreConverter;
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, StringToGenreConverter stringToGenreConverter) {
        this.movieRepository = movieRepository;
        this.stringToGenreConverter = stringToGenreConverter;
    }

    @Override
    public List<Movie> getAllMovies() {
        logger.debug("Fetching all movies from the repository...");
        List<Movie> movies = movieRepository.findAll();
        if (movies == null || movies.isEmpty()) {
            logger.warn("No movies found.");
            throw new MovieNotFoundException("Movie not found.", 404, "Not found");
        }
        logger.debug("Retrieved {} movies from the repository.", movies.size());
        return movies;
    }

    @Override
    public List<Movie> getMoviesByGenreAndRating(Genre genre, Double rating) {
        logger.debug("Fetching movies with genre: {} and rating: {}", genre, rating);
        List<Movie> filteredMovies = movieRepository.findAll().stream()
                .filter(movie -> (genre == null || movie.getGenre().equals(genre)))
                .filter(movie -> (rating == null || movie.getRating() >= rating))
                .collect(Collectors.toList());
        logger.debug("Found {} movies matching the criteria.", filteredMovies.size());
        return filteredMovies;
    }

    @Override
    public void addMovie(Movie movie) {
        logger.debug("Adding movie: {}", movie.getTitle());
        movieRepository.save(movie);
        logger.info("Movie '{}' added successfully.", movie.getTitle());
    }

    @Override
    public List<Movie> getFilteredMovies(String genreInput, Double rating) {
        logger.debug("Filtering movies by genre: {} and rating: {}", genreInput, rating);
        Genre genre = null;
        if (genreInput != null && !genreInput.isEmpty()) {
            genre = stringToGenreConverter.convert(genreInput);
        }
        List<Movie> filteredMovies = getMoviesByGenreAndRating(genre, rating);
        logger.debug("Found {} filtered movies.", filteredMovies.size());
        return filteredMovies;
    }

    @Override
    public Movie findByIdWithCinemas(Long id) {
        logger.debug("Fetching movie with id: {}", id);
        Movie movie = movieRepository.findByIdWithCinemas(id);
        if (movie == null) {
            logger.warn("Movie with id '{}' not found.", id);
            throw new MovieNotFoundException("Movie not found. Id:" + id, 404, "Not found");
        }
        logger.debug("Movie with id '{}' retrieved successfully.", id);
        return movie;
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("Deleting movie with id: {}", id);
        movieRepository.deleteById(id);
        logger.info("Movie with id '{}' deleted successfully.", id);
    }

    @Override
    public Movie findByTitle(String title) {
        logger.debug("Fetching movie with title: {}", title);
        Movie movie = movieRepository.findByTitle(title);
        if (movie == null) {
            logger.warn("Movie with title '{}' not found.", title);
        }
        return movie;
    }

    @Override
    public List<Movie> findByReleaseDateAfter(LocalDate releaseDate) {
        logger.debug("Fetching movies released after: {}", releaseDate);
        List<Movie> movies = movieRepository.findByReleaseDateAfter(releaseDate);
        logger.debug("Found {} movies released after the specified date.", movies.size());
        return movies;
    }

    @Override
    public List<MovieDTO> getAllMoviesForJson() {
        logger.debug("Converting all movies to DTO format...");
        List<Movie> movies = movieRepository.findAll();
        List<MovieDTO> movieDTOs = MovieMapper.toDTOList(movies);
        logger.debug("Converted {} movies to DTO format.", movieDTOs.size());
        return movieDTOs;
    }
}
