package org.example.projectcinema.dto;

import java.time.LocalDate;
import java.util.List;

public record MovieDTO(
        Long id,
        String title,
        LocalDate releaseDate,
        double rating,
        String genre,
        String image,
        List<CinemaSummaryDTO> cinemas,
        List<CinemaScreenSummaryDTO> screens
) {}
