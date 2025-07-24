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
                try {
                    map.put(counter, new ArrayList<>(Collections.singleton(Technique.fromString(trimmedString))));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid technique: " + trimmedString);
                }
            }
            counter++;
        }
        ArrayList<ArrayList<Technique>> result = new ArrayList<>();
        result.add(new ArrayList<>());

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

    private void printAvailableTechniques() {
        System.out.println("\nAvailable techniques:");
        System.out.println("- Hit (any hit type)");
        for (Technique technique : Technique.values()) {
            System.out.printf("- %s (%d points)\n", technique.toString().toLowerCase(), technique.getPoints());
        }
    }

    private String getValidRulesInput() {
        while (true) {
            try {
                System.out.print("\nEnter rules (from left to right, comma-separated): ");
                String input = getUserInput();
                if (input.trim().isEmpty()) {
                    System.out.println("Please enter at least one technique");
                    continue;
                }
                // Test if the input is valid by trying to parse it
                getTechniquesFromString(input);
                return input;
            } catch (IllegalArgumentException | IllegalAccessException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    private int getValidTargetInput() {
        while (true) {
            try {
                System.out.print("Enter target points: ");
                String input = getUserInput();
                int target = getIntFromString(input);
                if (target <= 0) {
                    System.out.println("Please enter a positive number");
                    continue;
                }
                return target;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== Terrafirmacraft Anvil Calculator ===");
            printAvailableTechniques();
            
            String rulesString = getValidRulesInput();
            int target = getValidTargetInput();

            System.out.println("\nWorking on it...");
            ArrayList<TechniqueGraph> allGraphs = new ArrayList<>();
            
            try {
                ArrayList<ArrayList<Technique>> rules = getTechniquesFromString(rulesString);

                for (ArrayList<Technique> rule : rules) {
                    Collections.reverse(rule);
                    TechniqueGraph graph = new TechniqueGraph(target, rule);
                    graph.getFullPlan();
                    allGraphs.add(graph);
                }

                String solution = Collections.max(allGraphs).getPathConsecutive();

                System.out.println("\n=== Best Course of Actions ===");
                if (solution == null) {
                    System.out.println("Can't solve graph!");
                } else {
                    System.out.println(solution);
                }
            } catch (Exception e) {
                System.out.println("Error calculating plan: " + e.getMessage());
            }

            System.out.print("\nWould you like to calculate another plan? (y/n): ");
            String response = scanner.nextLine().toLowerCase();
            if (!response.startsWith("y")) {
                System.out.println("Goodbye!");
                break;
            }
        }
    }
}
