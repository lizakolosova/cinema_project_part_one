package org.example.projectcinema.service.impl;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.MovieDTO;
import org.example.projectcinema.dto.mapper.MovieMapper;
import org.example.projectcinema.exceptions.MovieNotFoundException;
import org.example.projectcinema.repository.MovieRepository;
import org.example.projectcinema.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);
    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieDTO> getAllMovies() {
        logger.debug("Fetching all movies");
        List<Movie> movies = movieRepository.findAll();
        logger.debug("Retrieved {} movies", movies.size());
        return MovieMapper.toDTOList(movies);
    }

    @Override
    public List<MovieDTO> getMoviesByGenreAndRating(Genre genre, Double minRating) {
        logger.debug("Fetching movies with genre: {} and minimum rating: {}", genre, minRating);

        List<Movie> movies = movieRepository.findAll().stream()
                .filter(movie -> genre == null || movie.getGenre().equals(genre))
                .filter(movie -> minRating == null || movie.getRating() >= minRating)
                .collect(Collectors.toList());

        logger.debug("Found {} movies matching criteria", movies.size());
        return MovieMapper.toDTOList(movies);
    }

    @Override
    @Transactional
    public MovieDTO save(Movie movie) {
        logger.debug("Saving movie: {}", movie.getTitle());

        Movie saved = movieRepository.save(movie);
        logger.info("Movie '{}' saved with id: {}", saved.getTitle(), saved.getId());

        return MovieMapper.toDTO(saved);
    }

    @Override
    public List<MovieDTO> getFilteredMovies(String genreInput, Double rating) {
        logger.debug("Filtering movies by genre: {} and rating: {}", genreInput, rating);

        Genre genre = null;
        if (genreInput != null && !genreInput.isEmpty()) {
            try {
                genre = Genre.valueOf(genreInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid genre: {}", genreInput);
                throw new IllegalArgumentException("Invalid genre: " + genreInput);
            }
        }

        return getMoviesByGenreAndRating(genre, rating);
    }

    @Override
    public MovieDTO findById(Long id) {
        logger.debug("Fetching movie with id: {}", id);
        Movie movie = movieRepository.findByIdWithCinemas(id);

        if (movie == null) {
            logger.warn("Movie with id '{}' not found", id);
            throw new MovieNotFoundException("Movie not found with id: " + id);
        }

        logger.debug("Movie with id '{}' retrieved successfully", id);
        return MovieMapper.toDTO(movie);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Deleting movie with id: {}", id);

        Movie movie = movieRepository.findByIdWithCinemas(id);
        if (movie == null) {
            throw new MovieNotFoundException("Cannot delete: Movie not found with id: " + id);
        }

        for (Cinema cinema : new ArrayList<>(movie.getCinemas())) {
            movie.removeCinema(cinema);
        }

        for (CinemaScreen screen : new ArrayList<>(movie.getScreens())) {
            screen.getMovies().remove(movie);
        }
        movie.getScreens().clear();

        movieRepository.deleteById(id);
        logger.info("Movie with id '{}' deleted successfully", id);
    }

    @Override
    public Optional<MovieDTO> findByTitle(String title) {
        logger.debug("Fetching movie with title: {}", title);
        Movie movie = movieRepository.findByTitle(title);
        return Optional.ofNullable(movie)
                .map(MovieMapper::toDTO);
    }

    @Override
    public List<MovieDTO> findByReleaseDateAfter(LocalDate date) {
        logger.debug("Fetching movies released after: {}", date);
        List<Movie> movies = movieRepository.findByReleaseDateAfter(date);
        logger.debug("Found {} movies released after date", movies.size());
        return MovieMapper.toDTOList(movies);
    }
}