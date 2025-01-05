package org.example.projectcinema.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CinemaScreenDTO {

    private Long id;
    private int screenNumber;
    private String screenType;
    private int size;

    public CinemaScreenDTO(Long id, int screenNumber, String screenType, int size) {
        this.id = id;
        this.screenNumber = screenNumber;
        this.screenType = screenType;
        this.size = size;
    }
}
