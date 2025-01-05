package org.example.projectcinema.dto.mapper;

import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.dto.CinemaScreenDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CinemaScreenMapper {

    public static CinemaScreenDTO toDTO(CinemaScreen screen) {
        return new CinemaScreenDTO(
                screen.getId(),
                screen.getScreenNumber(),
                screen.getScreenType(),
                screen.getSize()
        );
    }

    public static List<CinemaScreenDTO> toDTOList(List<CinemaScreen> screens) {
        return screens.stream()
                .map(CinemaScreenMapper::toDTO)
                .collect(Collectors.toList());
    }
}

