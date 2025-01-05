package org.example.projectcinema.dto.mapper;

import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.MovieDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MovieMapper {

    public static MovieDTO toDTO(Movie movie) {
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getRating(),
                movie.getGenre().toString(),
                movie.getImage()
        );
    }

    public static List<MovieDTO> toDTOList(List<Movie> movies) {
        return movies.stream()
                .map(MovieMapper::toDTO)
                .collect(Collectors.toList());
    }
}

