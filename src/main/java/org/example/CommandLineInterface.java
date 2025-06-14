package org.example;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class CommandLineInterface {

    private String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().toUpperCase();
    }

    private int getIntFromString(String userInput) {
        return Integer.parseInt(userInput);
    }

    private ArrayList<ArrayList<Technique>> getTechniquesFromString(String userInput) throws IllegalAccessException {
        String[] strings = userInput.split(",");
        Technique[] hits = {Technique.HEAVY_HIT, Technique.MEDIUM_HIT, Technique.LIGHT_HIT};
        HashMap<Integer, ArrayList<Technique>> map = new HashMap<>();
        int counter = 0;

        for (String tech : strings) {
            String trimmedString = tech.trim();
            if (trimmedString.equalsIgnoreCase("hit")) {
                map.put(counter, new ArrayList<>(Arrays.asList(hits)));
            } else {
                map.put(counter, new ArrayList<>(Collections.singleton(Technique.fromString(trimmedString))));
            }
            counter++;
        }
        ArrayList<ArrayList<Technique>> result = new ArrayList<>();
        result.add(new ArrayList<>());
//        log.info("Map: {}", map);
        for (List<Technique> values : map.values()) {
            ArrayList<ArrayList<Technique>> temp = new ArrayList<>();

            for (ArrayList<Technique> combination : result) {
                for (Technique technique : values) {
                    ArrayList<Technique> newCombination = new ArrayList<>(combination);
                    newCombination.add(technique);
                    temp.add(newCombination);
                }
            }
            result = temp;
        }
        return result;
    }

    public void run() {
        System.out.println("Write Rules (from left to right)");
        System.out.print("List of available techniques: Hit (any), ");
        for (Technique technique : Technique.values()) System.out.printf("%s (%d), ", technique.toString().toLowerCase(), technique.getPoints());
        System.out.println("\b\b\nExample of your input: draw, hIT, Punch");
        System.out.print("Your input: ");
        String rulesString = this.getUserInput();
        ArrayList<ArrayList<Technique>> rules;
        try {
            rules = getTechniquesFromString(rulesString);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
//        log.debug("Making graph with {} rules", rules);

        System.out.print("Write Target: ");
        String targetString = getUserInput();
        int target = getIntFromString(targetString);
//        log.debug("Making graph with {} target", target);
        System.out.println("Working on it!");
        ArrayList<TechniqueGraph> allGraphs = new ArrayList<>();
        for (ArrayList<Technique> rule : rules) {
            TechniqueGraph graph = new TechniqueGraph(target, rule);
            graph.getFullPlan();
            allGraphs.add(graph);
        }

        TechniqueGraph bestGraph = Collections.max(allGraphs);
        System.out.println("--- Here is best course of actions ---");
        System.out.println(bestGraph.getPathConsecutive());
    }
}
