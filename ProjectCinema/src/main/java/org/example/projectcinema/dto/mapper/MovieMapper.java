package org.example.projectcinema.dto.mapper;

import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.example.projectcinema.dto.MovieDTO;
import org.example.projectcinema.dto.MovieSummaryDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MovieMapper {

    public static MovieDTO toDTO(Movie movie) {
        if (movie == null) {
            return null;
        }

        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getRating(),
                movie.getGenre() != null ? movie.getGenre().name() : null,
                movie.getImage(),
                movie.getCinemas() != null
                        ? movie.getCinemas().stream()
                        .map(CinemaMapper::toSummaryDTO)
                        .collect(Collectors.toList())
                        : List.of(),
                movie.getScreens() != null
                        ? movie.getScreens().stream()
                        .map(CinemaScreenMapper::toSummaryDTO)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }

    public static MovieSummaryDTO toSummaryDTO(Movie movie) {
        if (movie == null) {
            return null;
        }

        return new MovieSummaryDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getRating(),
                movie.getGenre() != null ? movie.getGenre().name() : null,
                movie.getImage()
        );
    }

    public static List<MovieDTO> toDTOList(List<Movie> movies) {
        if (movies == null) {
            return List.of();
        }

        return movies.stream()
                .map(MovieMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static Movie toEntity(MovieDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Movie(
                dto.title(),
                dto.releaseDate(),
                dto.rating(),
                Genre.valueOf(dto.genre()),
                dto.image()
        );
    }
}