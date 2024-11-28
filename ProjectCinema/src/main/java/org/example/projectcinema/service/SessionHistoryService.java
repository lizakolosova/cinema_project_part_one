package org.example.projectcinema.service;

import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface SessionHistoryService {
    public void addPageVisit(HttpSession session, String pageName);
    public List<String> getHistory(HttpSession session);
}
