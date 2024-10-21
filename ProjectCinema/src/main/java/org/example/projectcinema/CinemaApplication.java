package org.example.projectcinema;

import org.example.projectcinema.repository.InMemoryCinemaRepository;
import org.example.projectcinema.repository.InMemoryMovieRepository;
import org.example.projectcinema.service.CinemaServiceImpl;
import org.example.projectcinema.service.MovieServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
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
    public CommandLineRunner run(DataFactory dataFactory) {
        return args -> {
            dataFactory.seedData();
        };
    }
}
