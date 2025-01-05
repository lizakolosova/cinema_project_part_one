package org.example.projectcinema.repository.springjpa;

import org.example.projectcinema.domain.Cinema;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("JPA")
public interface CinemaRepositorySpringJPA extends JpaRepository<Cinema, Long> {
    Optional<Cinema> findByName(String name);

    List<Cinema> findByAddress(String address);

    List<Cinema> findCinemaByCapacityAfter(int capacity);

    @Query("SELECT c FROM Cinema c LEFT JOIN FETCH c.movies WHERE c.id = :id")
    Cinema findByIdWithMovies(Long id);
}

