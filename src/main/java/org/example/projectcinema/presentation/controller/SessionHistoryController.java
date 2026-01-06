package org.example.projectcinema.presentation.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectcinema.service.impl.SessionHistoryServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionHistoryController {

    private final SessionHistoryServiceImpl sessionHistoryService;

    public SessionHistoryController(SessionHistoryServiceImpl sessionHistoryService) {
        this.sessionHistoryService = sessionHistoryService;
    }

    @GetMapping("/session-history")
    public String showSessionHistory(HttpSession session, Model model) {
        model.addAttribute("history", sessionHistoryService.getHistory(session));
        return "session-history";
    }
}

