package org.example;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Data
@Slf4j
public class ActionGraph {

    @Data
    @NoArgsConstructor
    private class Node {
        private Node parent;
        private ArrayList<Node> children = new ArrayList<>();
        private ActionTypes action;
        private int points;
        private int currentPoints;
        private boolean initialized = false;

        public Node(ActionTypes action, Node parent) {
            this.parent = parent;
            this.action = action;
            this.points = this.action.getPoints();
            this.currentPoints = parent.getCurrentPoints() - this.points;
        }

        public void initializeChildren() {
            if (!this.initialized) {
                log.info("Initializing children for node with {} action", this.action);
                for (ActionTypes action : ActionTypes.values()) {
                    this.children.add(new Node(action, this));
                    this.initialized = true;
                }
            } else log.warn("Tried to initialize initialized node with {} action", this.action);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "children=" + children +
                    ", action=" + action +
                    ", points=" + points +
                    ", initialized=" + initialized +
                    ", currentPoints=" + currentPoints +
                    '}';
        }
    }


    int targetPoints;
    ArrayList<ActionTypes> fullPath = new ArrayList<>();
    ArrayList<Node> graph;
    Node rootNode;

    public ActionGraph(int targetPoints, ArrayList<ActionTypes> endSequence) {
        this.fullPath.addAll(endSequence);
        this.targetPoints = targetPoints - endSequence
                .stream()
                .map(ActionTypes::getPoints)
                .reduce(0, Integer::sum);
        log.info("{}, points to calculate with ending sequence {}. {} points actually.",
                targetPoints, endSequence, this.targetPoints);
        log.info("Making root node.");
        this.rootNode = new Node();
        this.rootNode.setCurrentPoints(this.targetPoints);
        log.info("root node: {}", rootNode);
        this.rootNode.initializeChildren();

    }

    public Node solveGraph() {
        Queue<Node> nodesQueue = new LinkedList<>(rootNode.getChildren());

        for (int depth = 0; depth < 10000000; depth++) {
            Node currentNode = nodesQueue.poll();
            log.info("trying {} node", currentNode);
            if (currentNode.getCurrentPoints() == 0) {
                log.info("Returning node {} with 0 target, final iterations {}:", currentNode, depth);
                return currentNode;
            }

            currentNode.initializeChildren();
            nodesQueue.addAll(currentNode.getChildren());
        }
        return null;
    }


    public void getFullPlan() {
        Node solvedGraph = this.solveGraph();
        while (solvedGraph.getParent() != null) {
            this.fullPath.addFirst(solvedGraph.getAction());
            solvedGraph = solvedGraph.getParent();
        }
        System.out.printf("Actions: %s;\nDepth: %d.", this.fullPath, this.fullPath.size());
    }


}
