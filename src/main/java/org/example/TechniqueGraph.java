package org.example;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

@Data
@Slf4j
public class TechniqueGraph {

    @Data
    @NoArgsConstructor
    private class Node {
        private Node parent;
        private ArrayList<Node> children = new ArrayList<>();
        private Technique technique;
        private int points;
        private int currentPoints;
        private int depth;
        private boolean initialized = false;

        public Node(Technique technique, Node parent) {
            this.parent = parent;
            this.technique = technique;
            this.points = technique.getPoints();
            this.currentPoints = parent.getCurrentPoints() - this.points;
            this.depth = parent.getDepth() + 1;
        }

        public void initializeChildren() {
            if (!this.initialized) {
//                log.debug("Initializing children for node with {} technique", this.technique);
                for (Technique technique : Technique.values()) {
                    this.children.add(new Node(technique, this));
                    this.initialized = true;
                }
            } else log.warn("Tried to initialize initialized node with {} technique", this.technique);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "children=" + children +
                    ", technique=" + technique +
                    ", points=" + points +
                    ", initialized=" + initialized +
                    ", currentPoints=" + currentPoints +
                    ", currentDepth=" + depth +
                    '}';
        }
    }


    int targetPoints;
    ArrayList<Technique> fullPath = new ArrayList<>();
    ArrayList<Node> graph;
    Node rootNode;

    public TechniqueGraph(int targetPoints, ArrayList<Technique> rules) {
        this.fullPath.addAll(rules);
        this.targetPoints = targetPoints - rules
                .stream()
                .map(Technique::getPoints)
                .reduce(0, Integer::sum);
        log.info("{}, points to calculate with ending rules {}. {} points actually.",
                targetPoints, rules, this.targetPoints);
        log.info("Making root node.");
        this.rootNode = new Node();
        this.rootNode.setCurrentPoints(this.targetPoints);
        this.rootNode.setDepth(1);
        this.rootNode.initializeChildren();
    }

    private Node searchBFS() {
        Queue<Node> nodesQueue = new LinkedList<>(rootNode.getChildren());

        int depth = 0;
        while (depth < 10) {
            Node currentNode = nodesQueue.poll();
//            log.debug("Trying {} node", currentNode);
            if (currentNode.getCurrentPoints() == 0) {
//                log.debug("Returning node {} with final iteration: {}.", currentNode, depth);
                return currentNode;
            }

            currentNode.initializeChildren();
            nodesQueue.addAll(currentNode.getChildren());
            depth = currentNode.getDepth();
        }
        log.warn("Graph is too deep!");
        return null;
    }


    private Node searchDFS() {
        Queue<Node> nodesQueue = new LinkedList<>(rootNode.getChildren());

        int depth = 0;
        while (depth < 100) {
            nodesQueue = new LinkedList<>(nodesQueue.stream().sorted(Comparator.comparing(Node::getPoints).reversed()).toList());
            Node currentNode = nodesQueue.poll();
            int currentPoints = currentNode.getCurrentPoints();
            if (currentPoints == 0) {
//                log.debug("Returning node {} with final iteration: {}.", currentNode, depth);
                return currentNode;
            } else if ((currentPoints < 0 || currentPoints > 200)) continue;
//            log.debug("Trying {} node", currentNode);

            currentNode.initializeChildren();
            nodesQueue.addAll(currentNode.getChildren());

            depth = currentNode.getDepth();

        }
        log.warn("Graph is too deep!");
        return null;
    }

    private String compress(Technique technique, int count) {
        if (count > 1)
            return String.format("%s✕%d, ", technique, count);
        else
            return String.format("%s, ", technique);
    }

    public void getFullPlan() {
//        Node resultNode = this.searchBFS();
        Node resultNode = this.searchDFS();
        while (resultNode.getParent() != null) {
            this.fullPath.addFirst(resultNode.getTechnique());
            resultNode = resultNode.getParent();
        }

        StringBuilder stringBuilder = new StringBuilder();
        int consecutiveTechniqueAmount = 1;
        for (int i = 0; i < this.fullPath.size(); i++) {
            Technique technique = this.fullPath.get(i);
            if (i + 2> this.fullPath.size()) {
                break;
            }
            Technique nextTechnique = this.fullPath.get(i + 1);
            if (technique == nextTechnique) {
                consecutiveTechniqueAmount += 1;
            } else {
                stringBuilder.append(this.compress(technique, consecutiveTechniqueAmount));
                consecutiveTechniqueAmount = 1;
            }
        }
        stringBuilder.append(this.compress(fullPath.getLast(), consecutiveTechniqueAmount));
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

        System.out.printf("Techniques (debug): %s;\nTechniques: %s;\nTotal actions: %d.", this.fullPath, stringBuilder, this.fullPath.size());
    }
}
