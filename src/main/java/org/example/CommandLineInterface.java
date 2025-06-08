package org.example;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

@Slf4j
public class CommandLineInterface {
    private String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().toUpperCase();
    }

    private ArrayList<Technique> getTechniqueListFromString(String userInput) {
        return Arrays.stream(userInput.replace(" ", "").split(",")).map(Technique::valueOf).collect(Collectors.toCollection(ArrayList::new));
    }

    private int getIntFromString(String userInput) {
        return Integer.parseInt(userInput);
    }

    public void run() {
        System.out.println("Write Rules (from left to right)");
        System.out.println("List of available techniques:");
        for (Technique technique : Technique.values()) System.out.printf("%s ", technique);
        System.out.println("\nExample of your input: draw, Heavy_Hit, Punch");
        System.out.println("Your input: ");
        String ruleString = getUserInput();
        ArrayList<Technique> rules = getTechniqueListFromString(ruleString);
//        log.debug("Making graph with {} rules", rules);
        System.out.println("Write Target:");
        String targetString = getUserInput();
        int target = getIntFromString(targetString);
//        log.debug("Making graph with {} target", target);

        TechniqueGraph graph = new TechniqueGraph(target, rules);

        graph.getFullPlan();
        System.out.println("\n=== Graph done! ===");
    }
}
