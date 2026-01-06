package org.example.projectcinema.service;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.dto.CinemaDTO;

import java.util.List;
import java.util.Optional;

public interface CinemaService {
    List<CinemaDTO> getAllCinemas();
    List<CinemaDTO> getCinemasByCapacity(int minCapacity);
    CinemaDTO save(Cinema cinema);
    CinemaDTO findById(Long id);
    void deleteById(Long id);
    Optional<CinemaDTO> findByName(String name);
    List<CinemaDTO> findByAddress(String address);
}