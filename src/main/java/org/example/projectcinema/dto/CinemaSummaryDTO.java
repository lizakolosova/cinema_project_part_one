package org.example.projectcinema.dto;

public record CinemaSummaryDTO(
        Long id,
        String name,
        String address,
        int capacity,
        String image
) {}
