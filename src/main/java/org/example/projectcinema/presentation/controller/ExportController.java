package org.example.projectcinema.presentation.controller;

import org.example.projectcinema.dto.CinemaDTO;
import org.example.projectcinema.dto.MovieDTO;
import org.example.projectcinema.service.CinemaService;
import org.example.projectcinema.service.ExportService;
import org.example.projectcinema.service.MovieService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
public class ExportController {

    private final CinemaService cinemaService;
    private final MovieService movieService;
    private final ExportService exportService;

    public ExportController(CinemaService cinemaService, MovieService movieService, ExportService exportService) {
        this.cinemaService = cinemaService;
        this.movieService = movieService;
        this.exportService = exportService;
    }

    @GetMapping("/export/cinemas")
    public ResponseEntity<InputStreamResource> exportCinemas() {

        List<CinemaDTO> cinemaDTOs = cinemaService.getAllCinemas();
        byte[] jsonData = exportService.exportCinemasToJson(cinemaDTOs);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=cinemas.json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(new ByteArrayInputStream(jsonData)));
    }

    @GetMapping("/export/movies")
    public ResponseEntity<InputStreamResource> exportMovies() {
        List<MovieDTO> movieDTOs = movieService.getAllMovies();
        byte[] jsonData = exportService.exportMoviesToJson(movieDTOs);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=movies.json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(new ByteArrayInputStream(jsonData)));
    }
}