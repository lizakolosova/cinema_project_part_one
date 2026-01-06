package org.example.projectcinema.exceptions;

public class CinemaNotFoundException extends RuntimeException {
    public CinemaNotFoundException(String message) {
        super(message);
    }
}

