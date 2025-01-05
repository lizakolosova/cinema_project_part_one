package org.example.projectcinema.service.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.MovieDTO;
import org.example.projectcinema.dto.mapper.MovieMapper;
import org.example.projectcinema.exceptions.MovieNotFoundException;
import org.example.projectcinema.presentation.converter.StringToGenreConverter;
import org.example.projectcinema.repository.jpa.MovieJPARepository;
import org.example.projectcinema.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile({"prod", "dev"})
public class MovieServiceJPA implements MovieService {

    private final MovieJPARepository movieRepository;
    private final StringToGenreConverter stringToGenreConverter;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(MovieServiceJPA.class);

    @Autowired
    public MovieServiceJPA(MovieJPARepository movieRepository, StringToGenreConverter stringToGenreConverter) {
        this.movieRepository = movieRepository;
        this.stringToGenreConverter = stringToGenreConverter;
    }

    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        logger.debug("Retrieved {} movies from the repository.", movies.size());
        return movies;
    }

    @Override
    public List<Movie> getMoviesByGenreAndRating(Genre genre, Double rating) {
        logger.debug("Filtering movies with genre: {} and rating: {}", genre, rating);
        List<Movie> filteredMovies = movieRepository.findAll().stream()
                .filter(movie -> (genre == null || movie.getGenre().equals(genre)))
                .filter(movie -> (rating == null || movie.getRating() >= rating))
                .collect(Collectors.toList());
        logger.debug("Found {} movies matching the criteria.", filteredMovies.size());
        return filteredMovies;
    }

    @Override
    @Transactional
    public void addMovie(Movie movie) {
        logger.debug("Saving movie: {}", movie.getTitle());
        movieRepository.save(movie);
        logger.debug("Saved movie with ID: {}", movie.getId());
    }

    @Override
    public List<Movie> getFilteredMovies(String genreInput, Double rating) {
        logger.debug("Filtering movies with input genre: {} and rating: {}", genreInput, rating);
        Genre genre = null;
        if (genreInput != null && !genreInput.isEmpty()) {
            genre = stringToGenreConverter.convert(genreInput);
            logger.debug("Converted genre input to: {}", genre);
        }

        List<Movie> filteredMovies = getMoviesByGenreAndRating(genre, rating);
        logger.debug("Found {} movies matching the filtered criteria.", filteredMovies.size());
        return filteredMovies;
    }

    @Override
    public Movie findByIdWithCinemas(Long id) {
        logger.debug("Searching for movie with ID: {}", id);
        Movie movie = movieRepository.findByIdWithCinemas(id);
        if (movie == null) {
            logger.error("Movie not found with ID: {}", id);
            throw new MovieNotFoundException("Movie not found. Id:" + id, 404, "Not found");
        }
        logger.debug("Found movie: {}", movie.getTitle());
        return movie;
    }

    @Override
    @Transactional
    public void deleteById(Long movieId) {
        logger.debug("Deleting movie with ID: {}", movieId);
        Movie movie = movieRepository.findById(movieId);
        if (movie == null) {
            logger.error("Movie not found with ID: {}", movieId);
            throw new MovieNotFoundException("Movie not found. Id:" + movieId, 404, "Not found");
        }

        // Remove the movie from associated screens
        for (CinemaScreen screen : new ArrayList<>(movie.getScreens())) {
            screen.getMovies().remove(movie);
            entityManager.merge(screen);
        }

        // Remove the movie from associated cinemas
        for (Cinema cinema : new ArrayList<>(movie.getCinemas())) {
            cinema.removeMovie(movie);
            entityManager.merge(cinema);
        }

        movieRepository.deleteById(movieId);
        logger.debug("Deleted movie with ID: {}", movieId);
    }

    @Override
    public Movie findByTitle(String title) {
        logger.debug("Searching for movie with title: {}", title);
        Movie movie = movieRepository.findByTitle(title);
        if (movie == null) {
            logger.error("Movie not found with title: {}", title);
        } else {
            logger.debug("Found movie: {}", movie.getTitle());
        }
        return movie;
    }

    @Override
    public List<Movie> findByReleaseDateAfter(LocalDate releaseDate) {
        logger.debug("Searching for movies released after: {}", releaseDate);
        List<Movie> movies = movieRepository.findByReleaseDateAfter(releaseDate);
        logger.debug("Found {} movies released after {}", movies.size(), releaseDate);
        return movies;
    }

    @Override
    public List<MovieDTO> getAllMoviesForJson() {
        logger.debug("Retrieving all movies for JSON conversion.");
        List<Movie> movies = movieRepository.findAll();
        List<MovieDTO> movieDTOs = MovieMapper.toDTOList(movies);
        logger.debug("Converted {} movies to DTOs.", movieDTOs.size());
        return movieDTOs;
    }
}


