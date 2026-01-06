package org.example.projectcinema.presentation.converter;

import lombok.extern.slf4j.Slf4j;
import org.example.projectcinema.presentation.viewmodel.CinemaViewModel;
import org.example.projectcinema.domain.Cinema;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
@Slf4j
public class CinemaViewModelToCinemaConverter implements Converter<CinemaViewModel, Cinema> {

    @Override
    public Cinema convert(CinemaViewModel source) {
        Cinema cinema = new Cinema();
        cinema.setName(source.getName());
        cinema.setAddress(source.getAddress());
        cinema.setCapacity(source.getCapacity());
        cinema.setImage(source.getImage());
        log.debug("CinemaModelView is converted to Cinema.");
        return cinema;
    }
}
