package org.example;

import lombok.Getter;

@Getter
public enum Technique {
    SHRINK(16),
    UPSET(13),
    BEND(7),
    PUNCH(2),
    DRAW(-15),
    HEAVY_HIT(-9),
    MEDIUM_HIT(-6),
    LIGHT_HIT(-3);

    private final int points;

    private Technique(int points) {
        this.points = points;
    }

    public static Technique fromString(String string) throws IllegalAccessException {
        for (Technique technique : Technique.values()) {
            if (string.charAt(0) == technique.name().charAt(0)) return technique;
        }
        throw new IllegalAccessException("No such technique: " + string);
    }
}
