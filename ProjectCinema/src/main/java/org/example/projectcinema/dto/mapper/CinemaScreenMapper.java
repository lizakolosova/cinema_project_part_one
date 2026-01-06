package org.example.projectcinema.dto.mapper;

import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.dto.CinemaScreenDTO;
import org.example.projectcinema.dto.CinemaScreenSummaryDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CinemaScreenMapper {

    public static CinemaScreenDTO toDTO(CinemaScreen screen) {
        if (screen == null) {
            return null;
        }

        return new CinemaScreenDTO(
                screen.getId(),
                screen.getScreenNumber(),
                screen.getScreenType(),
                screen.getSize(),
                screen.getCinema() != null ? screen.getCinema().getId() : null,
                screen.getCinema() != null ? screen.getCinema().getName() : null,
                screen.getMovies() != null
                        ? screen.getMovies().stream()
                        .map(MovieMapper::toSummaryDTO)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }

    public static CinemaScreenSummaryDTO toSummaryDTO(CinemaScreen screen) {
        if (screen == null) {
            return null;
        }

        return new CinemaScreenSummaryDTO(
                screen.getId(),
                screen.getScreenNumber(),
                screen.getScreenType(),
                screen.getSize()
        );
    }

    public static List<CinemaScreenDTO> toDTOList(List<CinemaScreen> screens) {
        if (screens == null) {
            return List.of();
        }

        return screens.stream()
                .map(CinemaScreenMapper::toDTO)
                .collect(Collectors.toList());
    }
}
