package org.example.projectcinema.presentation.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.projectcinema.presentation.converters.CinemaViewModelToCinemaConverter;
import org.example.projectcinema.presentation.viewmodels.CinemaViewModel;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.service.CinemaService;
import org.example.projectcinema.service.SessionHistoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
public class CinemaController {

    private static final Logger logger = LoggerFactory.getLogger(CinemaController.class);

    private final CinemaService cinemaService;
    private final CinemaViewModelToCinemaConverter converter;
    private final SessionHistoryServiceImpl sessionHistoryService;

    @Autowired
    public CinemaController(CinemaService cinemaService, CinemaViewModelToCinemaConverter converter, SessionHistoryServiceImpl sessionHistoryService) {
        this.cinemaService = cinemaService;
        this.converter = converter;
        this.sessionHistoryService = sessionHistoryService;
    }

    @GetMapping("/cinemas")
    public String getAllCinemas(Model model, HttpSession session) {
        logger.info("Fetching all cinemas");
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        logger.debug("Number of cinemas fetched: {}", cinemas.size());
        model.addAttribute("cinemas", cinemas);
        sessionHistoryService.addPageVisit(session, "All cinemas Page");
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
    public String showAddCinemaForm(Model model, HttpSession session) {
        logger.info("Displaying form to add new cinema");
        model.addAttribute("cinemaViewModel", new CinemaViewModel());
        sessionHistoryService.addPageVisit(session,"Add cinema Page");
        return "addcinema";
    }

    @PostMapping("/addcinema")
    public String addCinema(
            @Valid @ModelAttribute("cinemaViewModel") CinemaViewModel cinemaViewModel,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            logger.warn("Form submission contains validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("cinemaViewModel", cinemaViewModel);
            return "addcinema";
        }

        logger.info("Processing cinema addition request: {}", cinemaViewModel.getName());

        Cinema cinema = converter.convert(cinemaViewModel);

        try {
            cinemaService.saveCinema(cinema);
            logger.info("Cinema added successfully: {}", cinema.getName());
        } catch (IllegalArgumentException e) {
            logger.error("Error while adding cinema: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "addcinema";
        }

        return "redirect:/cinemas";
    }

    @GetMapping("/cinema-details/{id}")
    public String viewCinemaDetails(@PathVariable int id, Model model, HttpSession session) {
        Cinema cinema = cinemaService.findByIdWithMovies(id);
        if (cinema == null) {
            model.addAttribute("error", "Cinema not found");
            return "error-page";
        }
        model.addAttribute("cinema", cinema);
        sessionHistoryService.addPageVisit(session,"Cinema details Page");
        return "cinema-details";
    }
    @GetMapping("/cinemas/delete/{id}")
    public String deleteCinema(@PathVariable int id, RedirectAttributes redirectAttributes) {
        cinemaService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Cinema deleted successfully.");
        return "redirect:/cinemas";
    }
}
