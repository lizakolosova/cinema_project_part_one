package org.example.projectcinema.presentation.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectcinema.service.collections.SessionHistoryServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final SessionHistoryServiceImpl sessionHistoryService;

    public HomeController(SessionHistoryServiceImpl sessionHistoryService) {
        this.sessionHistoryService = sessionHistoryService;
    }

    @GetMapping("/home")
    public String getAllCinemas(HttpSession session) {
       sessionHistoryService.addPageVisit(session, "Home Page");
        return "index";
    }
}
