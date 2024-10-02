package org.example.projectcinema;

import org.example.projectcinema.presentation.CinemaConsolePresenter;
import org.example.projectcinema.repository.InMemoryCinemaRepository;
import org.example.projectcinema.repository.InMemoryMovieRepository;
import org.example.projectcinema.service.CinemaServiceImpl;
import org.example.projectcinema.service.MovieServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;


@SpringBootApplication
public class CinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }
    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public InMemoryCinemaRepository cinemaRepository() {
        return new InMemoryCinemaRepository();
    }

    @Bean
    public InMemoryMovieRepository movieRepository() {
        return new InMemoryMovieRepository();
    }

    @Bean
    public CinemaServiceImpl cinemaService(InMemoryCinemaRepository cinemaRepository) {
        return new CinemaServiceImpl(cinemaRepository);
    }

    @Bean
    public MovieServiceImpl movieService(InMemoryMovieRepository movieRepository) {
        return new MovieServiceImpl(movieRepository);
    }

    @Bean
    public CommandLineRunner run(CinemaConsolePresenter cinemaConsolePresenter) {
        return args -> {
            cinemaConsolePresenter.run();
        };
    }
    @Bean
    CommandLineRunner dataSeedingRunner(CinemaConsolePresenter cinemaConsolePresenter,
                                        InMemoryCinemaRepository cinemaRepository,
                                        InMemoryMovieRepository movieRepository) {
        return args -> {
            DataFactory.seedData(cinemaRepository, movieRepository);
            cinemaConsolePresenter.run();
        };
    }
}
