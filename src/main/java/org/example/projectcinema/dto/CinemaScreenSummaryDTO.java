package org.example.projectcinema.dto;

public record CinemaScreenSummaryDTO(
        Long id,
        int screenNumber,
        String screenType,
        int size
) {}