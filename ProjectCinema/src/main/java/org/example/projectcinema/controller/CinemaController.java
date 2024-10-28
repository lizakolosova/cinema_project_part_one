package org.example.projectcinema.controller;

import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.service.CinemaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CinemaController {

    private static final Logger logger = LoggerFactory.getLogger(CinemaController.class);

    private final CinemaService cinemaService;

    @Autowired
    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/cinemas")
    public String getAllCinemas(Model model) {
        logger.info("Fetching all cinemas");
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        logger.debug("Number of cinemas fetched: {}", cinemas.size());
        model.addAttribute("cinemas", cinemas);
        return "cinemas";
    }

    @PostMapping("/cinemas/filter")
    public String getCinemasByCapacity(@RequestParam("minCapacity") int minCapacity, Model model) {
        logger.info("Filtering cinemas by minimum capacity: {}", minCapacity);
        List<Cinema> filteredCinemas = cinemaService.getCinemasByCapacity(minCapacity);
        logger.debug("Number of cinemas after filtering: {}", filteredCinemas.size());
        model.addAttribute("cinemas", filteredCinemas);
        return "cinemas";
    }

    @GetMapping("/addcinema")
    public String showAddCinemaForm(Model model) {
        logger.info("Displaying form to add new cinema");
        model.addAttribute("cinema", new Cinema());
        return "addcinema";
    }

    @PostMapping("/addcinema")
    public String addCinema(@ModelAttribute Cinema cinema, Model model) {
        logger.info("Processing cinema addition request: {}", cinema.getName());

        if (cinema.getName() == null || cinema.getName().isEmpty()) {
            logger.warn("Cinema name is empty. Cannot proceed with adding cinema.");
            model.addAttribute("error", "Cinema name cannot be empty");
            return "addcinema";
        }

        if (cinema.getImage() == null || cinema.getImage().isEmpty()) {
            logger.warn("Image name is empty. Cannot proceed with adding cinema.");
            model.addAttribute("error", "Image name cannot be empty");
            return "addcinema";
        }

        logger.debug("Saving cinema: {} with image: {}", cinema.getName(), cinema.getImage());
        cinemaService.saveCinema(cinema);
        logger.info("Cinema added successfully: {} with image: {}", cinema.getName(), cinema.getImage());
        return "redirect:/cinemas";
    }
    @GetMapping("/cinema-details")
    public String getCinemaDetails(@RequestParam String name, Model model) {
        logger.debug("Received cinema name: {}", name);
        Cinema cinema = cinemaService.findByName(name);
        if (cinema == null) {
            model.addAttribute("error", "Cinema not found.");
            return "error-page";
        }
        model.addAttribute("cinema", cinema);
        return "cinema-details";
    }

}