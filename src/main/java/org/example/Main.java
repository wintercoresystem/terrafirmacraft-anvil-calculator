package org.example;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        // TODO: Add DFG for target > 100
        // TODO: Add multiprocessing
        // TODO: Add ambiguous hit
        // TODO: Make cache
        // TODO: Fix memory overflow
        ArrayList<Technique> rules = new ArrayList<>(Arrays.asList(
                Technique.PUNCH, // Last action
                Technique.HEAVY_HIT, // Second to last action
                Technique.UPSET) // Thrid to last action
                .reversed());
        int points = 55; // Points goal
        TechniqueGraph graph = new TechniqueGraph(points, rules);

        graph.getFullPlan();

    }
}