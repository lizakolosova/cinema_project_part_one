package org.example.projectcinema.domain;

public enum Genre {
    ACTION, COMEDY, DRAMA, HORROR, ANIMATION, ADVENTURE, THRILLER, SCIFI, ROMANCE;
public static Genre fromString(String genre) {
    for (Genre g : values()) {
        if (g.name().equalsIgnoreCase(genre)) {
            return g;
        }
    }
    throw new IllegalArgumentException("Invalid genre: " + genre);
    }
}