package org.example.projectcinema.repository;

import org.example.projectcinema.domain.Cinema;
import java.util.ArrayList;
import java.util.List;

public class InMemoryCinemaRepository implements CinemaRepository {
    private final List<Cinema> cinemas = new ArrayList<>();

    @Override
    public List<Cinema> findAll() {
        return new ArrayList<>(cinemas);
    }

    @Override
    public void save(Cinema cinema) {
        if (cinema == null) {
            throw new IllegalArgumentException("Cinema cannot be null");
        }

        for (Cinema existingCinema : cinemas) {
            if (existingCinema.getName().equals(cinema.getName())) {
                System.out.println("Cinema with this name already exists: " + cinema.getName());
                return;
            }
        }

        cinemas.add(cinema);
    }
}
