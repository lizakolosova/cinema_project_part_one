package org.example.projectcinema.dto.mapper;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.dto.CinemaDTO;
import org.example.projectcinema.dto.CinemaSummaryDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CinemaMapper {

    public static CinemaDTO toDTO(Cinema cinema) {
        if (cinema == null) {
            return null;
        }

        return new CinemaDTO(
                cinema.getId(),
                cinema.getName(),
                cinema.getAddress(),
                cinema.getCapacity(),
                cinema.getImage(),
                cinema.getScreens() != null
                        ? cinema.getScreens().stream()
                        .map(CinemaScreenMapper::toSummaryDTO)
                        .collect(Collectors.toList())
                        : List.of(),
                cinema.getMovies() != null
                        ? cinema.getMovies().stream()
                        .map(MovieMapper::toSummaryDTO)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }

    public static CinemaSummaryDTO toSummaryDTO(Cinema cinema) {
        if (cinema == null) {
            return null;
        }

        return new CinemaSummaryDTO(
                cinema.getId(),
                cinema.getName(),
                cinema.getAddress(),
                cinema.getCapacity(),
                cinema.getImage()
        );
    }

    public static List<CinemaDTO> toDTOList(List<Cinema> cinemas) {
        if (cinemas == null) {
            return List.of();
        }

        return cinemas.stream()
                .map(CinemaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static Cinema toEntity(CinemaDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Cinema(
                dto.name(),
                dto.address(),
                dto.capacity(),
                dto.image()
        );
    }
}
