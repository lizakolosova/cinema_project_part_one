package org.example.projectcinema.service;

import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface SessionHistoryService {
    void addPageVisit(HttpSession session, String pageName);
    List<String> getHistory(HttpSession session);
}
