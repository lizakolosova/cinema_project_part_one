package org.example.projectcinema.repository.springjpa;

import org.example.projectcinema.domain.CinemaScreen;
import org.example.projectcinema.repository.CinemaScreenRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("JPA")
public class CinemaScreenRepositorySpringJpaAdapter implements CinemaScreenRepository {

    private final CinemaScreenSpringDataJpaRepository screenRepository;

    public CinemaScreenRepositorySpringJpaAdapter(CinemaScreenSpringDataJpaRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    public CinemaScreen save(CinemaScreen cinemaScreen) {
        return screenRepository.save(cinemaScreen);
    }

}