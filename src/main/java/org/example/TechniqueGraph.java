package org.example;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Data
@Slf4j
public class TechniqueGraph implements Comparable<TechniqueGraph> {

    @Data
    @NoArgsConstructor
    private static class Node {
        private Node parent;
        private List<Node> children = new ArrayList<>();
        private Technique technique;
        private int points;
        private int currentPoints;
        private int depth;

        public Node(Technique technique, Node parent) {
            this.parent = parent;
            this.technique = technique;
            this.points = technique.getPoints();
            this.currentPoints = parent.getCurrentPoints() - this.points;
            this.depth = parent.getDepth() + 1;
        }

        public void initializeChildren() {
            if (children.isEmpty()) {
                for (Technique technique : Technique.values()) {
                    if (this.currentPoints - technique.getPoints() >= 0) {
                        this.children.add(new Node(technique, this));
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Node{" +
                    "technique=" + technique +
                    ", points=" + points +
                    ", currentPoints=" + currentPoints +
                    ", depth=" + depth +
                    '}';
        }
    }

    private final int targetPoints;
    private final List<Technique> fullPath = new ArrayList<>();
    private final Node rootNode;
    private int finalDepth;
    private int uniqueTechniques;
    private String pathConsecutive;

    public TechniqueGraph(int targetPoints, List<Technique> rules) {
        this.fullPath.addAll(rules);
        this.targetPoints = targetPoints - rules.stream()
                .mapToInt(Technique::getPoints)
                .sum();

        this.rootNode = new Node();
        this.rootNode.setCurrentPoints(this.targetPoints);
        this.rootNode.setDepth(1);
        this.rootNode.initializeChildren();
    }

    private Node searchDFS() {
        PriorityQueue<Node> nodesQueue = new PriorityQueue<>(
                Comparator.comparing(Node::getPoints).reversed()
        );
        nodesQueue.addAll(rootNode.getChildren());

        while (!nodesQueue.isEmpty()) {
            Node currentNode = nodesQueue.poll();
            int currentPoints = currentNode.getCurrentPoints();

            if (currentPoints == 0) {
                this.finalDepth = currentNode.getDepth();
                return currentNode;
            }

            if (currentPoints < 0 || currentPoints > 200) {
                continue;
            }

            currentNode.initializeChildren();
            nodesQueue.addAll(currentNode.getChildren());
        }

        return null;
    }

    private String compress(Technique technique, int count) {
        return count > 1 ?
                String.format("%s✕%d, ", technique, count) :
                String.format("%s, ", technique);
    }

    public void getFullPlan() {
        Node resultNode = this.searchDFS();
        if (resultNode == null) {
            return;
        }

        while (resultNode.getParent() != null) {
            this.fullPath.addFirst(resultNode.getTechnique());
            resultNode = resultNode.getParent();
        }

        StringBuilder stringBuilder = new StringBuilder();
        int consecutiveCount = 1;
        Technique currentTechnique = null;

        for (Technique technique : this.fullPath) {
            if (currentTechnique == null) {
                currentTechnique = technique;
                continue;
            }

            if (technique == currentTechnique) {
                consecutiveCount++;
            } else {
                stringBuilder.append(compress(currentTechnique, consecutiveCount));
                currentTechnique = technique;
                consecutiveCount = 1;
            }
        }

        if (currentTechnique != null) {
            stringBuilder.append(compress(currentTechnique, consecutiveCount));
        }

        if (stringBuilder.length() > 2) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }

        this.pathConsecutive = stringBuilder.toString();
        this.uniqueTechniques = (int) this.pathConsecutive.chars().filter(ch -> ch == ' ').count() + 1;
    }

    @Override
    public int compareTo(TechniqueGraph otherGraph) {
        int depthCompare = Integer.compare(otherGraph.getFinalDepth(), this.finalDepth);
        return depthCompare != 0 ? depthCompare :
                Integer.compare(otherGraph.getUniqueTechniques(), this.uniqueTechniques);
    }
}
