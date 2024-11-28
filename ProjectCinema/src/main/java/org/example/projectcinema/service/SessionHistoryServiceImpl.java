package org.example.projectcinema.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionHistoryServiceImpl implements SessionHistoryService {

    public void addPageVisit(HttpSession session, String pageName) {
        List<String> history = (List<String>) session.getAttribute("pageHistory");
        if (history == null) {
            history = new ArrayList<>();
        }

        String entry = pageName + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        history.add(0,entry);

        session.setAttribute("pageHistory", history);
    }

    public List<String> getHistory(HttpSession session) {
        List<String> history = (List<String>) session.getAttribute("pageHistory");
        return history != null ? history : new ArrayList<>();
    }
}