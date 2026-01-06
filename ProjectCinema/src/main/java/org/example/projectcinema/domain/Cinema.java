package org.example.projectcinema.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Positive(message = "Capacity must be a positive number.")
    private int capacity;

    @OneToMany(mappedBy = "cinema", orphanRemoval = true)
    private List<CinemaScreen> screens;

    private String image;

    @ManyToMany
    @JoinTable(
            name = "cinema_movies",
            joinColumns = @JoinColumn(name = "cinema_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private List<Movie> movies = new ArrayList<>();

    public Cinema(Long id, String name, String address, int capacity, String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.image = image;
        this.screens = new ArrayList<>();
        this.movies = new ArrayList<>();
    }
    public Cinema(String name, String address, int capacity, String image) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.image = image;
        this.screens = new ArrayList<>();
        this.movies = new ArrayList<>();
    }

    public void addMovie(Movie movie) {
        if (!this.movies.contains(movie)) {
            this.movies.add(movie);
        }
        if (!movie.getCinemas().contains(this)) {
            movie.getCinemas().add(this);
        }
    }

    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.getCinemas().remove(this);
    }

    public void addScreen(CinemaScreen screen) {
        if (!this.screens.contains(screen)) {
            this.screens.add(screen);
            screen.setCinema(this);
        }
    }

    public void removeScreen(CinemaScreen screen) {
        this.screens.remove(screen);
        screen.setCinema(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cinema cinema)) return false;
        return id != null && id.equals(cinema.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Cinema: " + name + ", Address: " + address + ", Capacity: " + capacity;
    }
}