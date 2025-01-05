package org.example.projectcinema.presentation.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.projectcinema.exceptions.CinemaNotFoundException;
import org.example.projectcinema.presentation.converter.CinemaViewModelToCinemaConverter;
import org.example.projectcinema.presentation.viewmodel.CinemaViewModel;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.service.CinemaService;
import org.example.projectcinema.service.collections.SessionHistoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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
        model.addAttribute("cinemaViewModel", new CinemaViewModel());
        sessionHistoryService.addPageVisit(session, "Add cinema Page");
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
        cinemaService.saveCinema(cinema);
        return "redirect:/cinemas";
    }
    @GetMapping("/cinema-details/{id}")
    public String viewCinemaDetails(@PathVariable Long id, Model model, HttpSession session) {
        try {
            Cinema cinema = cinemaService.findByIdWithMovies(id);
            model.addAttribute("cinema", cinema);
            sessionHistoryService.addPageVisit(session, "Cinema details Page");
            return "cinema-details";
        } catch (CinemaNotFoundException ex) {
            logger.error("Cinema not found with id: {}", id);
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("condition", ex.getCondition());
            model.addAttribute("time",  ex.getTime());
            return "other-error";
        }
    }
    @GetMapping("/cinemas/delete/{id}")
    public String deleteCinema(@PathVariable Long id) {
        cinemaService.deleteById(id);
        return "redirect:/cinemas";
    }

    @GetMapping("/name")
    public String searchByNameForm(@RequestParam(required = false) String name, Model model) {
            model.addAttribute("cinemas", cinemaService.findByName(name));
        return "cinemas-by-name";
    }

    @PostMapping("/name")
    public String searchByName(@RequestParam("name") String name, Model model) {
        model.addAttribute("cinemas", cinemaService.findByName(name));
        return "cinemas-by-name";
    }

    @GetMapping("/address")
    public String searchByAddressForm(@RequestParam(required = false) String address, Model model) {
            model.addAttribute("cinemas", cinemaService.findCinemasByAddress(address));
        return "cinemas-by-address";
    }

    @PostMapping("/address")
    public String searchByAddress(@RequestParam("address") String address, Model model) {
        model.addAttribute("cinemas", cinemaService.findCinemasByAddress(address));
        logger.info("Fetching cinemas by address: {}", address);
        return "cinemas-by-address";
    }
}
