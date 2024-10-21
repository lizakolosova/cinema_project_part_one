package org.example.projectcinema.repository;

import org.example.projectcinema.domain.Movie;

import java.util.ArrayList;
import java.util.List;

public class InMemoryMovieRepository implements MovieRepository {
    private final List<Movie> movies = new ArrayList<>();

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>(movies);
    }

    @Override
    public void save(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }

        for (Movie existingMovie : movies) {
            if (existingMovie.getTitle().equals(movie.getTitle())) {
                System.out.println("Movie with this title already exists: " + movie.getTitle());
                return;
        }

        movies.add(movie);
    }
}
}
