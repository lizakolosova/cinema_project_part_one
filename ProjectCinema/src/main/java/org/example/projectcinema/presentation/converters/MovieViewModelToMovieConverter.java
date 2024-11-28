package org.example.projectcinema.presentation.converters;

import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.presentation.viewmodels.MovieViewModel;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class MovieViewModelToMovieConverter implements Converter<MovieViewModel, Movie> {
    @Override
    public Movie convert(MovieViewModel source) {
        Movie movie = new Movie();
        movie.setTitle(source.getTitle());
        movie.setReleaseDate(source.getReleaseDate());
        movie.setGenre(source.getGenre());
        movie.setRating(source.getRating());
        movie.setImage(source.getImage());
        return movie;
    }
}
