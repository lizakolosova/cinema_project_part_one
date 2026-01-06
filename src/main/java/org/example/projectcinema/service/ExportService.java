package org.example.projectcinema.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectcinema.dto.CinemaDTO;
import org.example.projectcinema.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportService {

   private final ObjectMapper objectMapper;

    @Autowired
    public ExportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] exportCinemasToJson(List<CinemaDTO> cinemaDTOs) {
        try {
            return objectMapper.writeValueAsBytes(cinemaDTOs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting cinemas to JSON", e);
        }
    }

    public byte[] exportMoviesToJson(List<MovieDTO> movieDTOs) {
        try {
            return objectMapper.writeValueAsBytes(movieDTOs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting movies to JSON", e);
        }
    }
}


