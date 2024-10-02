package org.example.projectcinema.service;


import org.example.projectcinema.domain.Cinema;

import java.util.List;

public interface CinemaService {
    List<Cinema> getAllCinemas();
    List<Cinema> getCinemasByCapacity(int minCapacity);
}
