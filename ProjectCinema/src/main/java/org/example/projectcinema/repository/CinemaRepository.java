package org.example.projectcinema.repository;

import org.example.projectcinema.domain.Cinema;

import java.util.List;

public interface CinemaRepository {
    List<Cinema> findAll();
    void save(Cinema cinema);
}
