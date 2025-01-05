package org.example.projectcinema.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CinemaDTO {

    private Long id;

    private String name;

    private String address;

    private int capacity;

    private String image;

    private List<CinemaScreenDTO> screens;
    private List<MovieDTO> movies;

    public CinemaDTO(Long id, String name, String address, int capacity, String image, List<CinemaScreenDTO> screens, List<MovieDTO> movies) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.image = image;
        this.screens = screens;
        this.movies = movies;
    }
}

