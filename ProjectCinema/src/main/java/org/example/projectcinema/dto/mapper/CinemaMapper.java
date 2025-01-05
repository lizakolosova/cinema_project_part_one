package org.example.projectcinema.dto.mapper;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CinemaMapper {

    public static CinemaDTO toDTO(Cinema cinema) {
        List<CinemaScreenDTO> screenDTOs = cinema.getScreens().stream()
                .map(CinemaScreenMapper::toDTO)
                .collect(Collectors.toList());

        List<MovieDTO> movieDTOs = cinema.getMovies().stream()
                .map(MovieMapper::toDTO)
                .collect(Collectors.toList());

        return new CinemaDTO(
                cinema.getId(),
                cinema.getName(),
                cinema.getAddress(),
                cinema.getCapacity(),
                cinema.getImage(),
                screenDTOs,
                movieDTOs
        );
    }

    public static List<CinemaDTO> toDTOList(List<Cinema> cinemas) {
        return cinemas.stream()
                .map(CinemaMapper::toDTO)
                .collect(Collectors.toList());

    }
}

