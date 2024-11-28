package org.example.projectcinema;

import org.example.projectcinema.presentation.converters.StringToGenreConverter;
import org.example.projectcinema.repository.*;
import org.example.projectcinema.service.CinemaServiceImpl;
import org.example.projectcinema.service.MovieServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class CinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Bean
    @Profile("jdbc")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Profile("jdbc")
    public JdbcMovieRepository jdbcMovieRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcMovieRepository(jdbcTemplate);
    }

    @Bean
    @Profile("jdbc")
    public JdbcCinemaRepository jdbcCinemaRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcCinemaRepository(jdbcTemplate);
    }

    @Bean
    public CinemaServiceImpl cinemaService(@Qualifier("jdbcCinemaRepository") CinemaRepository cinemaRepository) {
        return new CinemaServiceImpl(cinemaRepository);
    }

    @Bean
    public MovieServiceImpl movieService(@Qualifier("jdbcMovieRepository") MovieRepository movieRepository, StringToGenreConverter stringToGenreConverter) {
        return new MovieServiceImpl(movieRepository, stringToGenreConverter);
    }
}
