package org.example.projectcinema.repository.springjpa;

import org.example.projectcinema.domain.Genre;
import org.example.projectcinema.domain.Movie;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Profile("JPA")
public interface MovieSpringDataJpaRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);

    List<Movie> findByReleaseDateAfter(LocalDate releaseDate);

    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.cinemas WHERE m.id = :id")
    Movie findByIdWithCinemas(@Param("id") Long id);

    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.screens WHERE m.id = :id")
    Movie findByIdWithScreens(@Param("id") Long id);

    @Query("SELECT m FROM Movie m " +
            "WHERE (:genre IS NULL OR m.genre = :genre) " +
            "AND (:rating IS NULL OR m.rating >= :rating)")
    List<Movie> findMoviesByGenreAndRating(
            @Param("genre") Genre genre,
            @Param("rating") Double rating
    );
}
