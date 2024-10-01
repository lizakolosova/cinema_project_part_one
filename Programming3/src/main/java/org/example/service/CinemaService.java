package org.example.service;

import org.example.domain.Cinema;

import java.util.List;

public interface CinemaService {
    List<Cinema> getAllCinemas();
    List<Cinema> getCinemasByCapacity(int minCapacity);
}
