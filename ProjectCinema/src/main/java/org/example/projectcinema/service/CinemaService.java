package org.example.projectcinema.service;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.dto.CinemaDTO;
import java.util.List;

public interface CinemaService {
    List<Cinema> getAllCinemas();
    List<Cinema> getCinemasByCapacity(int minCapacity);
    void saveCinema(Cinema cinema);
    Cinema findByIdWithMovies(Long id);
    void deleteById(Long id);
    Cinema findByName(String name);
    List<Cinema> findCinemasByAddress(String address);
    List<CinemaDTO> getAllCinemasForJson();
}
