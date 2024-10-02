package org.example.projectcinema.service;


import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.repository.CinemaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository cinemaRepository;

    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    @Override
    public List<Cinema> getCinemasByCapacity(int minCapacity) {
        return cinemaRepository.findAll().stream()
                .filter(cinema -> cinema.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
    }
}
