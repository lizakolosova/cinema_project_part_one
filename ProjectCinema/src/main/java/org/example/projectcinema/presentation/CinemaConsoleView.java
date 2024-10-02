package org.example.projectcinema.presentation;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.domain.Movie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class CinemaConsoleView implements CinemaView {
    private final Scanner scanner;

    public CinemaConsoleView(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void showMenu() {
        System.out.println("What would you like to do?");
        System.out.println("==========================");
        System.out.println("0) Quit");
        System.out.println("1) Show all cinemas");
        System.out.println("2) Show cinemas by capacity");
        System.out.println("3) Show all movies");
        System.out.println("4) Show movies by genre and rating");
        System.out.print("Choice (0-4): ");
    }

    @Override
    public int getUserChoice() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine()); // Read as String and parse
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number (0-4): ");
            }
        }
    }

    @Override
    public void showAllCinemas(List<Cinema> cinemas) {
        System.out.println("All Cinemas:");
        if (cinemas.isEmpty()) {
            System.out.println("No cinemas available.");
        } else {
            cinemas.forEach(System.out::println);
        }
    }

    @Override
    public void showFilteredCinemas(List<Cinema> cinemas) {
        System.out.println("Filtered Cinemas:");
        if (cinemas.isEmpty()) {
            System.out.println("No cinemas match your criteria.");
        } else {
            cinemas.forEach(System.out::println);
        }
    }

    @Override
    public void showAllMovies(List<Movie> movies) {
        System.out.println("All Movies:");
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
        } else {
            movies.forEach(System.out::println);
        }
    }

    @Override
    public void showFilteredMovies(List<Movie> movies) {
        System.out.println("Filtered Movies:");
        if (movies.isEmpty()) {
            System.out.println("No movies match your criteria.");
        } else {
            movies.forEach(System.out::println);
        }
    }

    @Override
    public String getGenreInput() {
        System.out.print("Enter genre: ");
        return scanner.nextLine();
    }

    @Override
    public Double getRatingInput() {
        System.out.print("Enter minimum rating (0.0 - 10.0) or leave blank: ");
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for rating. Defaulting to null.");
            return null;
        }
    }

    @Override
    public int getMinCapacity() {
        System.out.print("Enter minimum capacity: ");
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine()); // Read as String and parse
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number for capacity: ");
            }
        }
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}