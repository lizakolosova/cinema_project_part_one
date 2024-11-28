package org.example.projectcinema.presentation.converters;

import org.example.projectcinema.presentation.viewmodels.CinemaViewModel;
import org.example.projectcinema.domain.Cinema;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class CinemaViewModelToCinemaConverter implements Converter<CinemaViewModel, Cinema> {

    @Override
    public Cinema convert(CinemaViewModel source) {
        Cinema cinema = new Cinema();
        cinema.setName(source.getName());
        cinema.setAddress(source.getAddress());
        cinema.setCapacity(source.getCapacity());
        cinema.setImage(source.getImage());
        return cinema;
    }
}
