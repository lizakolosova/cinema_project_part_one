package org.example.projectcinema.repository;

import org.example.projectcinema.domain.Cinema;

import java.util.List;

public interface CinemaRepository {
    List<Cinema> findAll();
    Cinema findById(int id);
    Cinema save(Cinema cinema);
    void update(Cinema cinema);
    void deleteById(int id);
    Cinema findByIdWithMovies(int id);
}
