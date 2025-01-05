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

    @ManyToMany(cascade = CascadeType.PERSIST)
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


    public void addScreens(CinemaScreen screen) {
        this.screens.add(screen);
    }

    public void addMovie(Movie movie) {
        if (!this.movies.contains(movie)) {
            this.movies.add(movie);
        }
    }
    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.getCinemas().remove(this);
    }

    @Override
    public String toString() {
        return "Cinema: " + name + ", Address: " + address + ", Capacity: " + capacity;
    }
}