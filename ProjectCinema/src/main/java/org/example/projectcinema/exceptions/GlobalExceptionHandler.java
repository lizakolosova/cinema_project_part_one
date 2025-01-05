package org.example.projectcinema.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import org.springframework.beans.TypeMismatchException;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TypeMismatchException.class)
    public String handleTypeMismatchException(TypeMismatchException ex, Model model) {
        log.error("Invalid genre provided. {}", ex.getMessage());
        if (ex.getPropertyName() != null && ex.getPropertyName().equals("genre")) {
            model.addAttribute("error", "Invalid genre provided. Please select a valid genre.");
        } else {
            model.addAttribute("error", "Invalid input provided. Please correct the form and try again.");
        }
        return "addmovie";
    }

    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseException(DataAccessException ex, Model model) {
        log.error("Database error occurred: {}", ex.getMessage());
        model.addAttribute("status", 500);
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("timestamp", LocalDateTime.now());
        return "database-error";
    }

//    @ExceptionHandler(MovieNotFoundException.class)
//    public String handleMovieNotFoundException(MovieNotFoundException ex, Model model) {
//        log.error(ex.getMessage());
//        model.addAttribute("message", ex.getMessage());
//        model.addAttribute("condition", ex.getCondition());
//        model.addAttribute("time",  ex.getTime());
//        return "other-error";
//    }
//
//    @ExceptionHandler(CinemaNotFoundException.class)
//    public String handleCinemaNotFoundException(CinemaNotFoundException ex, Model model) {
//        log.error(ex.getMessage());
//        model.addAttribute("message", ex.getMessage());
//        model.addAttribute("condition", ex.getCondition());
//        model.addAttribute("time",  ex.getTime());
//        return "other-error";
//    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        log.error(ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("condition", "Application error");
        model.addAttribute("time",  LocalDateTime.now());
        return "other-error";
    }
}
