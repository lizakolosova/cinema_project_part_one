package org.example.repository;
import org.example.domain.Cinema;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCinemaRepository implements CinemaRepository {
    private List<Cinema> cinemas = new ArrayList<>();

    @Override
    public List<Cinema> findAll() {
        return cinemas;
    }

    @Override
    public void save(Cinema cinema) {
        cinemas.add(cinema);
    }
}
