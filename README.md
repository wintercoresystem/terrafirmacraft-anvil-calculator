# TFC Forge Optimizer CLI

A command-line tool to find the optimal forging sequences in the TerraFirmaCraft (TFC) Minecraft mod using graph traversal and DFS algorithms.

## Features

- Calculates the most efficient forging sequences to reach target metal properties
- Supports all TFC forging techniques (draw, punch, heavy/medium/light hit, etc.)
- Handles flexible input rules (e.g., "draw, hit, punch" or "hit, bnd, puch")
- Finds the shortest valid path to achieve the target value
- Optimized with graph algorithms for performance

## Installation

1. Ensure you have Java 8 or later installed
2. Download the latest JAR file from [Releases]
3. Run with: `java -jar TerrafirmacraftAnvilCalculator.jar`

## Usage

```
Write Rules (from right to left (first to last))
List of available techniques: Hit (any), light_hit, medium_hit, heavy_hit, draw, punch, bend, upset, shrink
Example of your input: draw, hIT, Punch
Your input: [Enter your forging sequence rules]
Write Target: [Enter your target value]
Working on it!
--- Here is best course of actions ---
[Optimal forging sequence will be displayed here]
```

### Input Format
- Separate techniques with commas
- "hit" will consider all hit types (heavy, medium, light)
- Example valid inputs:
  - `draw, hit, punch`
  - `b, hit, s`
  - `upset, punch, draw`

## How It Works

The tool uses a graph-based approach:
1. Builds all possible forging sequences based on your rules
2. Creates a graph of possible technique combinations
3. Uses DFS algorithm to find paths that reach the target value
4. Selects the shortest valid path as the optimal solution

## Building from Source

1. Clone the repository
2. Build with Maven: `mvn clean package`
3. Find the JAR in the `target` directory

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you'd like to change.
