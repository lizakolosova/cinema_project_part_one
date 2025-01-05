package org.example.projectcinema.presentation.converter;

import lombok.extern.slf4j.Slf4j;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.presentation.viewmodel.MovieViewModel;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
@Slf4j
public class MovieViewModelToMovieConverter implements Converter<MovieViewModel, Movie> {
    @Override
    public Movie convert(MovieViewModel source) {
        Movie movie = new Movie();
        movie.setTitle(source.getTitle());
        movie.setReleaseDate(source.getReleaseDate());
        movie.setGenre(source.getGenre());
        movie.setRating(source.getRating());
        movie.setImage(source.getImage());
        log.debug("MovieModelView is converted to Movie.");
        return movie;
    }
}
