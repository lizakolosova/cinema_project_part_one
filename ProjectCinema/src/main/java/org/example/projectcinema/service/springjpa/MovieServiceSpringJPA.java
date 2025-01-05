package org.example.projectcinema.service.springjpa;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.dto.MovieDTO;
import org.example.projectcinema.dto.mapper.MovieMapper;
import org.example.projectcinema.exceptions.MovieNotFoundException;
import org.example.projectcinema.presentation.converter.StringToGenreConverter;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.repository.springjpa.MovieRepositorySpringJPA;
import org.example.projectcinema.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Profile("JPA")
public class MovieServiceSpringJPA implements MovieService {

    private final MovieRepositorySpringJPA movieRepository;
    private final StringToGenreConverter stringToGenreConverter;

    @Autowired
    public MovieServiceSpringJPA(MovieRepositorySpringJPA movieRepository, StringToGenreConverter stringToGenreConverter) {
        this.movieRepository = movieRepository;
        this.stringToGenreConverter = stringToGenreConverter;
    }

    @Transactional
    @Override
    public void addMovie(Movie movie) {
        log.debug("Attempting to save movie: {}", movie.getTitle());
            if (movieRepository.findByTitle(movie.getTitle()).isPresent()) {
                log.warn("Movie with the title '{}' already exists. Skipping save.", movie.getTitle());
                return;
            }
            movieRepository.save(movie);
            log.info("Movie '{}' saved successfully.", movie.getTitle());

    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> getMoviesByGenreAndRating(Genre genre, Double rating) {
        return movieRepository.findMoviesByGenreAndRating(genre, rating);
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
    public void deleteById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with ID " + id + " not found for deletion.", 404 , "Not found"));

        for (CinemaScreen screen : new ArrayList<>(movie.getScreens())) {
            screen.getMovies().remove(movie);
        }

        for (Cinema cinema : new ArrayList<>(movie.getCinemas())) {
            cinema.removeMovie(movie);
        }

        movieRepository.deleteById(id);
        log.info("Movie with ID {} deleted successfully.", id);
    }


    @Override
    public Movie findByIdWithCinemas(Long id) {
        Movie movie = movieRepository.findByIdWithCinemas(id);
        if (movie == null) {
            throw new MovieNotFoundException("Movie not found. Id:" + id, 404, "Not found");
        }
        return movie;
    }

    @Override
    public Movie findByTitle(String title) {
        return movieRepository.findByTitle(title)
                .orElse(null);
    }

    @Override
    public List<Movie> findByReleaseDateAfter(LocalDate releaseDate){
        return movieRepository.findByReleaseDateAfter(releaseDate);
    }

    @Override
    public List<MovieDTO> getAllMoviesForJson() {
        List<Movie> movies = movieRepository.findAll();
        return MovieMapper.toDTOList(movies);
    }
}
