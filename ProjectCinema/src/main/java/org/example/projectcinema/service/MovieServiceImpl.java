package org.example.projectcinema.service;


import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
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
    public Movie findByTitle(String title) {
        String trimmedTitle = title.trim();
        logger.debug("Searching for movie with name: {}", trimmedTitle);

        List<Movie> movies = movieRepository.findAll();
        logger.debug("Current movies in the repository: {}", movies.stream().map(Movie::getTitle).collect(Collectors.toList()));

        return movies.stream()
                .filter(cinema -> {
                    boolean matches = cinema.getTitle().equalsIgnoreCase(trimmedTitle);
                    logger.debug("Comparing with movie: {}. Matches: {}", cinema.getTitle(), matches);
                    return matches;
                })
                .findFirst()
                .orElse(null);
    }
}