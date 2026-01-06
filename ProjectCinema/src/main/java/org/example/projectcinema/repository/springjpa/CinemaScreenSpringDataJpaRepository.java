package org.example.projectcinema.repository.springjpa;

import org.example.projectcinema.domain.CinemaScreen;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Profile("JPA")
public interface CinemaScreenSpringDataJpaRepository extends JpaRepository<CinemaScreen, Long> {

    @Query("SELECT cs FROM CinemaScreen cs WHERE cs.screenNumber = :screenNumber AND cs.cinema.id = :cinemaId")
    CinemaScreen findExistingCinemaScreen(@Param("screenNumber") Integer screenNumber, @Param("cinemaId") Long cinemaId);
}

