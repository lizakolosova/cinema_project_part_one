package org.example.projectcinema.dto;

import java.time.LocalDate;

public record MovieSummaryDTO(
        Long id,
        String title,
        LocalDate releaseDate,
        double rating,
        String genre,
        String image
) {}
