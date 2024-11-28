package org.example.projectcinema.domain;
;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
    private int Id;
    private String name;
    private String address;
    @Positive(message = "Capacity must be a positive number.")
    private int capacity;
    private List<CinemaScreen> screens = new ArrayList<>();
    private String image;
    private List<Movie> movies = new ArrayList<>();

    public Cinema(int Id, String name, String address, int capacity, String image) {
        this.Id = Id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.image = image;
    }
    public Cinema(String name, String address, int capacity, String image) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.image = image;
        this.screens = new ArrayList<>();
    }
    public Cinema() {
        this.screens = new ArrayList<>();
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<CinemaScreen> getScreens() {
        return screens;
    }
    public void setScreens(List<CinemaScreen> screens) {}
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public void addScreens(CinemaScreen screen) {
        this.screens.add(screen);
    }


    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }


    @Override
    public String toString() {
        return "Cinema: " + name + ", Address: " + address + ", Capacity: " + capacity;
    }
}