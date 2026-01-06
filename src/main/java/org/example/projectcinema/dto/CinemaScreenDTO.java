package org.example.projectcinema.dto;

import java.util.List;

public record CinemaScreenDTO(
        Long id,
        int screenNumber,
        String screenType,
        int size,
        Long cinemaId,
        String cinemaName,
        List<MovieSummaryDTO> movies
) {}
