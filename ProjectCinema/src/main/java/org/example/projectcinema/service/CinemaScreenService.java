package org.example.projectcinema.service;

import org.example.projectcinema.domain.CinemaScreen;
import org.springframework.stereotype.Service;

@Service
public interface CinemaScreenService {
    CinemaScreen saveCinemaScreen(CinemaScreen cinemaScreen);
}
