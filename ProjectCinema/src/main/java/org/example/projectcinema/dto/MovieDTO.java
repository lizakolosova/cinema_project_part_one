package org.example.projectcinema.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MovieDTO {

    private Long id;
    private String title;
    private LocalDate releaseDate;
    private double rating;

    private String genre;
    private String image;

    public MovieDTO(Long id, String title, LocalDate releaseDate, double rating, String genre, String image) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.genre = genre;
        this.image = image;
    }
}

