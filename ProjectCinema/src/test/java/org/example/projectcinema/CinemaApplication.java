package org.example.projectcinema;
import org.example.projectcinema.presentation.converters.StringToGenreConverter;
import org.example.projectcinema.repository.CinemaRepository;
import org.example.projectcinema.repository.InMemoryMovieRepository;
import org.example.projectcinema.repository.JdbcCinemaRepository;
import org.example.projectcinema.repository.JdbcMovieRepository;
import org.example.projectcinema.repository.MovieRepository;
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
    public CinemaRepository cinemaRepository() {
        return new JdbcCinemaRepository();
    }

    @Bean
    public MovieRepository movieRepository() {
        return new JdbcMovieRepository();
    }

    @Bean
    public CinemaServiceImpl cinemaService(CinemaRepository cinemaRepository) {
        return new CinemaServiceImpl(cinemaRepository);
    }

    @Bean
    public MovieServiceImpl movieService(MovieRepository movieRepository, StringToGenreConverter stringToGenreConverter) {
        return new MovieServiceImpl(movieRepository, stringToGenreConverter);
    }
}