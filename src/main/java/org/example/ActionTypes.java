package org.example;

public enum ActionTypes {
    SHRINK(16),
    UPSET(13),
    BEND(7),
    PUNCH(2),
    DRAW(-15),
    HEAVY_HIT(-9),
    MEDIUM_HIT(-6),
    LIGHT_HIT(-3);

    private int points;

    private ActionTypes(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public static ActionTypes getAction(int points) {
        for (ActionTypes action : ActionTypes.values()) {
            if (action.getPoints() == points) {
                return action;
            }
        }
        return null;
    }
}
