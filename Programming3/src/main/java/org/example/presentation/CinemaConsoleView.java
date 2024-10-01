package org.example.presentation;

import org.example.domain.Cinema;
import org.example.domain.Movie;

import java.util.List;
import java.util.Scanner;

public class CinemaConsoleView implements CinemaView {
    private final Scanner scanner = new Scanner(System.in);

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
        return scanner.nextInt();
    }

    @Override
    public void showAllCinemas(List<Cinema> cinemas) {
        System.out.println("All Cinemas:");
        cinemas.forEach(System.out::println);
    }

    @Override
    public void showFilteredCinemas(List<Cinema> cinemas) {
        System.out.println("Filtered Cinemas:");
        cinemas.forEach(System.out::println);
    }

    @Override
    public void showAllMovies(List<Movie> movies) {
        System.out.println("All Movies:");
        movies.forEach(System.out::println);
    }

    @Override
    public void showFilteredMovies(List<Movie> movies) {
        System.out.println("Filtered Movies:");
        movies.forEach(System.out::println);
    }

    @Override
    public String getGenreInput() {
        System.out.print("Enter genre: ");
        return scanner.next();
    }

    @Override
    public Double getRatingInput() {
        System.out.print("Enter minimum rating (0.0 - 10.0) or leave blank: ");
        String input = scanner.next();
        return input.isEmpty() ? null : Double.parseDouble(input);
    }

    @Override
    public int getMinCapacity() {
        System.out.print("Enter minimum capacity: ");
        return scanner.nextInt();
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}