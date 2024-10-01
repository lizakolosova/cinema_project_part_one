package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
    private String name;
    private String address;
    private int capacity;
    private List<CinemaScreen> screens;

    public Cinema(String name, String address, int capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.screens = new ArrayList<>();
    }

    public void addScreen(CinemaScreen screen) {
        screens.add(screen);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<CinemaScreen> getScreens() {
        return screens;
    }

    @Override
    public String toString() {
        return "Cinema: " + name + ", Address: " + address + ", Capacity: " + capacity;
    }
}
