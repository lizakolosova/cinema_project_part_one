package org.example.projectcinema.presentation.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.projectcinema.dto.CinemaDTO;
import org.example.projectcinema.exceptions.CinemaNotFoundException;
import org.example.projectcinema.presentation.converter.CinemaViewModelToCinemaConverter;
import org.example.projectcinema.presentation.viewmodel.CinemaViewModel;
import org.example.projectcinema.domain.Cinema;
import org.example.projectcinema.service.CinemaService;
import org.example.projectcinema.service.SessionHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final SessionHistoryService sessionHistoryService;

    public CinemaController(
            CinemaService cinemaService,
            CinemaViewModelToCinemaConverter converter,
            SessionHistoryService sessionHistoryService) {
        this.cinemaService = cinemaService;
        this.converter = converter;
        this.sessionHistoryService = sessionHistoryService;
    }

    @GetMapping("/cinemas")
    public String getAllCinemas(Model model, HttpSession session) {
        logger.info("Fetching all cinemas");

        List<CinemaDTO> cinemas = cinemaService.getAllCinemas();

        logger.debug("Number of cinemas fetched: {}", cinemas.size());

        model.addAttribute("cinemas", cinemas);
        sessionHistoryService.addPageVisit(session, "All cinemas Page");

        return "cinemas";
    }

    @PostMapping("/cinemas/filter")
    public String getCinemasByCapacity(@RequestParam("minCapacity") int minCapacity, Model model) {
        logger.info("Filtering cinemas by minimum capacity: {}", minCapacity);

        List<CinemaDTO> filteredCinemas = cinemaService.getCinemasByCapacity(minCapacity);

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
            Model model) {

        if (bindingResult.hasErrors()) {
            logger.warn("Form submission contains validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("cinemaViewModel", cinemaViewModel);
            return "addcinema";
        }

        logger.info("Processing cinema addition request: {}", cinemaViewModel.getName());

        Cinema cinema = converter.convert(cinemaViewModel);

        cinemaService.save(cinema);

        return "redirect:/cinemas";
    }

    @GetMapping("/cinema-details/{id}")
    public String viewCinemaDetails(@PathVariable Long id, Model model, HttpSession session) {
        try {
            CinemaDTO cinema = cinemaService.findById(id);

            model.addAttribute("cinema", cinema);
            sessionHistoryService.addPageVisit(session, "Cinema details Page");

            return "cinema-details";

        } catch (CinemaNotFoundException ex) {
            logger.error("Cinema not found with id: {}", id);
            model.addAttribute("message", ex.getMessage());
            return "other-error";
        }
    }

    @PostMapping("/cinemas/delete/{id}")
    public String deleteCinema(@PathVariable Long id) {
        logger.info("Deleting cinema with id: {}", id);
        cinemaService.deleteById(id);
        return "redirect:/cinemas";
    }

    @GetMapping("/address")
    public String searchByAddressForm(@RequestParam(required = false) String address, Model model) {
        if (address != null && !address.isEmpty()) {
            List<CinemaDTO> cinemas = cinemaService.findByAddress(address);
            model.addAttribute("cinemas", cinemas);
        }
        return "cinemas-by-address";
    }

    @PostMapping("/address")
    public String searchByAddress(@RequestParam("address") String address, Model model) {
        logger.info("Fetching cinemas by address: {}", address);

        List<CinemaDTO> cinemas = cinemaService.findByAddress(address);
        model.addAttribute("cinemas", cinemas);

        return "cinemas-by-address";
    }
}