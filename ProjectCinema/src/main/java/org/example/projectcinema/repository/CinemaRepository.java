package org.example.projectcinema.repository;

import org.example.projectcinema.domain.Cinema;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository {
    List<Cinema> findAll();
//    Cinema findById(Long id);
    Cinema save(Cinema cinema);
//    void update(Cinema cinema);
    void deleteById(Long id);
    Cinema findByIdWithMovies(Long id);
    Cinema findByName(String name);
    List<Cinema> findByAddress(String address);
}
