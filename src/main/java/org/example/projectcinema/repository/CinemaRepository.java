package org.example.projectcinema.repository;

import org.example.projectcinema.domain.Cinema;

import java.util.List;

public interface CinemaRepository {
    List<Cinema> findAll();
    Cinema save(Cinema cinema);
    void deleteById(Long id);
    Cinema findByIdWithMovies(Long id);
    Cinema findByName(String name);
    List<Cinema> findByAddress(String address);
}
