package org.example;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ArrayList<ActionTypes> endSequence = new ArrayList<>(Arrays.asList(
                ActionTypes.PUNCH,
                ActionTypes.LIGHT_HIT,
                ActionTypes.LIGHT_HIT)
                .reversed());
        int points = 60;
        ActionGraph graph = new ActionGraph(points, endSequence);

        graph.getFullPlan();

    }
}