package org.example.projectcinema.service.collections;

import jakarta.servlet.http.HttpSession;
import org.example.projectcinema.service.SessionHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionHistoryServiceImpl implements SessionHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(SessionHistoryServiceImpl.class);

    @Override
    public void addPageVisit(HttpSession session, String pageName) {
        logger.info("Adding page visit for '{}' to session.", pageName);

        List<String> history = (List<String>) session.getAttribute("pageHistory");
        if (history == null) {
            history = new ArrayList<>();
            logger.debug("No previous history found in session, creating a new list.");
        }

        String entry = pageName + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        history.add(0, entry);

        session.setAttribute("pageHistory", history);

        logger.info("Page visit for '{}' added to session. Current history size: {}", pageName, history.size());
    }

    @Override
    public List<String> getHistory(HttpSession session) {
        logger.info("Fetching page history from session.");

        List<String> history = (List<String>) session.getAttribute("pageHistory");
        if (history == null) {
            logger.warn("No page history found in session.");
            history = new ArrayList<>();
        }

        logger.info("Retrieved page history from session. History size: {}", history.size());
        return history;
    }
}
