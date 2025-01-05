package org.example.projectcinema.presentation.viewmodel;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.projectcinema.domain.Genre;

import java.time.LocalDate;


public class MovieViewModel {
    @NotBlank(message = "Title is required.")
    private String title;

    @NotNull(message = "Release date is required.")
    private LocalDate releaseDate;

    @NotNull(message = "Rating cannot be null")
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.")
    @DecimalMax(value = "10.0", message = "Rating must not exceed 10.")
    private Double rating;

    @NotNull(message = "Genre is required.")
    private Genre genre;

    @NotBlank(message = "Image name is required.")
    private String image;

    public Genre getGenre() {
        return genre;
    }

    public double getRating() {
        return this.rating != null ? this.rating : 0.0;
    }

    public String getTitle() {
        return title;
    }


    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return title + " (" + genre + ") - " + releaseDate + " - Rating: " + rating;
    }
}
