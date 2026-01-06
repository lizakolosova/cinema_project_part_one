package org.example.projectcinema.dto;

import java.util.List;

public record CinemaDTO(
        Long id,
        String name,
        String address,
        int capacity,
        String image,
        List<CinemaScreenSummaryDTO> screens,
        List<MovieSummaryDTO> movies
) {}