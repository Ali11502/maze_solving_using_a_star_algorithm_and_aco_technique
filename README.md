# Maze Solver README

This project provides a Java implementation of two algorithms to solve a maze: A* algorithm and Ant Colony Optimization (ACO). You can input a maze, specify the start and goal positions, and choose between the two algorithms or run both to compare their performance.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
  - [Input Format](#input-format)
  - [Running the Program](#running-the-program)
- [Algorithms](#algorithms)
  - [A* Algorithm](#a-algorithm)
  - [Ant Colony Optimization (ACO)](#ant-colony-optimization-aco)

## Installation

1. Ensure you have Java installed on your machine. You can download it from the [official Java website](https://www.oracle.com/java/technologies/javase-downloads.html).
2. Download or clone this repository to your local machine.

## Usage

### Input Format

The maze is represented as a 2D array where:
- `0` represents a solid node (closed).
- `1` represents an open node.
- `2` represents the start node.
- `3` represents the goal node.

For example, a 3x3 maze looks like this:
```
0 1 2
0 0 1
3 1 1
```

### Running the Program

1. Compile the Java files using a Java compiler. In the command line, navigate to the directory containing the files and run:
   ```
   javac Main.java Node.java AstarAlgorithm.java ACOMazeSolver.java
   ```

2. Run the program:
   ```
   java Main
   ```

3. Follow the prompts:
   - Enter the number of rows in the maze.
   - Enter the number of columns in the maze.
   - Paste the entire 2D array representing the maze. Ensure the elements are separated by spaces.

4. Choose the desired operation:
   - Press `0` to find the path using the A* algorithm.
   - Press `1` to find the optimized path using ACO.
   - Press `2` to run both algorithms and compare their performance.
   - Press `3` to exit the program.

### Example

For the example maze:
```
0 1 2
0 0 1
3 1 1
```
Input `3` for rows, `3` for columns, and paste the above array when prompted.

## Algorithms

### A* Algorithm

The A* algorithm is a popular pathfinding algorithm that uses a heuristic to estimate the cost to reach the goal from the current node. It explores the most promising nodes first, using the formula:
```
f(n) = g(n) + h(n)
```
where `g(n)` is the cost from the start node to the current node, and `h(n)` is the estimated cost from the current node to the goal.

### Ant Colony Optimization (ACO)

ACO is a probabilistic technique inspired by the behavior of ants searching for food. It uses pheromone trails to find the shortest path. Ants deposit pheromone on paths they take, and the probability of choosing a path increases with the amount of pheromone present.

The key parameters in ACO are:
- **ALPHA**: Importance of pheromone levels in probability calculation.
- **BETA**: Importance of heuristic information in probability calculation.
- **Evaporation Rate**: The rate at which pheromone evaporates.

---
