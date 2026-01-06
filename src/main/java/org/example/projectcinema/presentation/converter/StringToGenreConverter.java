package org.example.projectcinema.presentation.converter;

import org.example.projectcinema.domain.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToGenreConverter implements Converter<String, Genre> {
    private static final Logger logger = LoggerFactory.getLogger(StringToGenreConverter.class);

    @Override
    public Genre convert(String source) {
        try {
            logger.info("Converting genre.");
            return Genre.fromString(source.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}
