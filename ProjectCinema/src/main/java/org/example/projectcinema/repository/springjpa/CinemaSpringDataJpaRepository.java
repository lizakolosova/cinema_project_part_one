package org.example.projectcinema.repository.springjpa;

import org.example.projectcinema.domain.Cinema;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Profile("JPA")
public interface CinemaSpringDataJpaRepository extends JpaRepository<Cinema, Long> {
    Optional<Cinema> findByName(String name);

    List<Cinema> findByAddress(String address);

    List<Cinema> findByCapacityGreaterThanEqual(int capacity);

    @Query("SELECT DISTINCT c FROM Cinema c " +
            "LEFT JOIN FETCH c.movies " +
            "WHERE c.id = :id")
    Cinema findByIdWithMovies(@Param("id") Long id);

    @Query("SELECT DISTINCT c FROM Cinema c " +
            "LEFT JOIN FETCH c.screens " +
            "WHERE c.id = :id")
    Cinema findByIdWithScreens(@Param("id") Long id);
}

