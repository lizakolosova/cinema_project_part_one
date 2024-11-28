package org.example.projectcinema.presentation.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import org.springframework.beans.TypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TypeMismatchException.class)
    public String handleTypeMismatchException(TypeMismatchException ex, Model model) {
        if (ex.getPropertyName() != null && ex.getPropertyName().equals("genre")) {
            model.addAttribute("error", "Invalid genre provided. Please select a valid genre.");
        } else {
            model.addAttribute("error", "Invalid input provided. Please correct the form and try again.");
        }
        return "addmovie";
    }
}
