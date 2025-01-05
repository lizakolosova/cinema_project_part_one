package org.example.projectcinema.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MovieNotFoundException extends RuntimeException {
    private final Integer condition;

    private final String error;

    private final LocalDateTime time;


    public MovieNotFoundException(String message, Integer condition, String error) {
        super(message);
        this.condition = condition;
        this.error = error;
        this.time = LocalDateTime.now();
    }
}
