package org.example.projectcinema.domain;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
    private String name;
    private String address;
    private int capacity;
    private List<CinemaScreen> screens;
    private String image;

    public Cinema(String name, String address, int capacity, String image) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.screens = new ArrayList<>();
        this.image = image;
    }
    public Cinema() {
    }

    public void addScreen(CinemaScreen screen) {
        screens.add(screen);
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
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Cinema: " + name + ", Address: " + address + ", Capacity: " + capacity;
    }
}