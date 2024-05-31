import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * ACOMazeSolver is a class that solves a maze using Ant Colony Optimization (ACO) algorithm.
 * It uses pheromone-based communication between ants to find the shortest path from the start cell to the goal cell in the maze.
 */

public class ACOMazeSolver {
    // Constants to represent different types of cells in the maze
    private static final int WALL = 0;
    private static final int OPEN = 1;
    private static final int START = 2;
    private static final int GOAL = 3;
    private int rows;                   // Number of rows in the maze
    private int columns;                // Number of columns in the maze
    private int[][] maze;               // Representation of the maze
    private double[][] pheromoneLevels; // Pheromone levels between cells
    private int startCell;              // Cell index of the start position
    private int goalCell;               // Cell index of the goal position
    private Random random;              // Random number generator

    private static final double ALPHA = 0.1; // Importance of pheromone levels in probability calculation
    private static final double BETA = 0.1;  // Importance of heuristic information in probability calculation

    /**
     * Constructor for ACOMazeSolver class.
     *
     * @param rows    Number of rows in the maze.
     * @param columns Number of columns in the maze.
     * @param maze    Representation of the maze.
     */
    public ACOMazeSolver(int rows, int columns, int[][] maze) {
        this.rows = rows;
        this.columns = columns;
        this.maze = maze;
        pheromoneLevels = new double[rows * columns][rows * columns];
        initializePheromoneLevels();
        random = new Random();

        // Find the start cell and goal cell in the maze
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (maze[i][j] == START) {
                    startCell = i * columns + j;
                } else if (maze[i][j] == GOAL) {
                    goalCell = i * columns + j;
                }
            }
        }
    }

    /**
     * Initializes the pheromone levels between cells in the maze.
     * Sets pheromone levels to a small positive value for open cells, and 0 for wall cells.
     */
    private void initializePheromoneLevels() {
        for (int i = 0; i < rows * columns; i++) {
            for (int j = 0; j < rows * columns; j++) {
                if (maze[i / columns][i % columns] != WALL && maze[j / columns][j % columns] != WALL) {
                    pheromoneLevels[i][j] = 0.01;
                }
            }
        }
    }

    /**
     * Formats the path found by an ant into a readable string representation.
     *
     * @param path       The path found by the ant.
     * @param pathLength The length of the path.
     * @return A formatted string representation of the path.
     */
    private String formatPath(int[] path, int pathLength) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pathLength; i++) {
            int cell = path[i];
            int row = cell / columns;
            int col = cell % columns;
            builder.append("(").append(row).append(",").append(col).append(")");
            if (i != pathLength - 1) {
                builder.append(" -> ");
            }
        }
        return builder.toString();
    }

    /**
     * Represents an ant that traverses the maze to find a path.
     */
    private class Ant {
        private int id;                 // Unique identifier for the ant
        private int currentCell;        // Current cell index
        private int[] path;             // Path taken by the ant
        private int pathLength;         // Length of the path
        private boolean[] visited;      // Array to track visited cells
        private Random random;

        /**
         * Constructor for Ant class.
         *
         * @param id Unique identifier for the ant.
         */
        public Ant(int id) {
            this.id = id;
            path = new int[(rows * columns)];
            pathLength = 0;
            visited = new boolean[rows * columns];
            currentCell = startCell;
            path[pathLength++] = currentCell;
            visited[currentCell] = true;
            random = new Random();
        }

        public void reset() {
            pathLength = 0;
            currentCell = startCell;
            path[pathLength++] = currentCell;
            Arrays.fill(visited, false);
            visited[currentCell] = true;
        }

        /**
         * Returns the path taken by the ant.
         *
         * @return An array representing the path.
         */
        public int[] getPath() {
            int[] result = new int[pathLength];
            System.arraycopy(path, 0, result, 0, pathLength);
            return result;
        }

        /**
         * Returns the length of the path taken by the ant.
         *
         * @return The length of the path.
         */
        public int getPathLength() {
            return pathLength;
        }

        /**
         * Runs the ant through the maze until it reaches the goal or the path length limit.
         */
        public void run() {
            while (currentCell != goalCell && path.length > pathLength) {
                int nextCell = selectNextCell();
                path[pathLength] = nextCell;
                visited[nextCell] = true;
                currentCell = nextCell;
                pathLength++;
            }
        }

        /**
         * Selects the next cell for the ant to move to, based on the probability values calculated.
         *
         * @return The index of the next cell to move to.
         */
        private int selectNextCell() {
            double[] probabilities = calculateProbabilities();
            double randomValue = random.nextDouble();
            int rand;

            double sum = 0;

            for (int i = 0; i < probabilities.length; i++) {
                sum += probabilities[i];
                if (randomValue < sum) {  // Use < instead of <=
                    // Convert the index to cell coordinates
                    int row = i / columns;
                    int col = i % columns;
                    return row * columns + col;
                } else rand = (int) (Math.random() * getNeighbors(currentCell).length);
                if (getNeighbors(currentCell).length != 0) {
                    return getNeighbors(currentCell)[rand];
                }

            }


            return path[pathLength - 2];
        }

        /**
         * Calculates the probability values for the ant to select each neighboring cell.
         *
         * @return An array of probabilities for each neighboring cell.
         */
        private double[] calculateProbabilities() {
            double[] probabilities = new double[rows * columns];
            double total = 0;

            for (int neighbor : getNeighbors(currentCell)) {
                if (!visited[neighbor]) {
                    double pheromoneLevel = pheromoneLevels[currentCell][neighbor];
                    double heuristic = getHeuristic(neighbor);
                    probabilities[neighbor] = Math.pow(pheromoneLevel, ALPHA) * Math.pow(heuristic, BETA);
                    total += probabilities[neighbor];
                }
            }

            // Normalize probabilities
            for (int i = 0; i < probabilities.length; i++) {
                probabilities[i] /= total;
            }

            return probabilities;
        }

        /**
         * Retrieves the neighboring cells of a given cell.
         *
         * @param cell The index of the cell.
         * @return An array of neighboring cells.
         */
        private int[] getNeighbors(int cell) {
            int row = cell / columns;
            int col = cell % columns;
            ArrayList<Integer> neighbors = new ArrayList<>();

            if (row > 0 && !visited[cell - columns] && maze[row - 1][col] != WALL) {
                neighbors.add(cell - columns); // Up
            }
            if (row < rows - 1 && !visited[cell + columns] && maze[row + 1][col] != WALL) {
                neighbors.add(cell + columns); // Down
            }
            if (col > 0 && !visited[cell - 1] && maze[row][col - 1] != WALL) {
                neighbors.add(cell - 1); // Left
            }
            if (col < columns - 1 && !visited[cell + 1] && maze[row][col + 1] != WALL) {
                neighbors.add(cell + 1); // Right
            }

            int[] result = new int[neighbors.size()];
            for (int i = 0; i < neighbors.size(); i++) {
                result[i] = neighbors.get(i);
            }

            return result;
        }

        /**
         * Calculates the heuristic value for a given cell, representing the distance to the goal cell.
         *
         * @param cell The index of the cell.
         * @return The heuristic value.
         */
        private double getHeuristic(int cell) {
            int row = cell / columns;
            int col = cell % columns;
            int goalRow = goalCell / columns;
            int goalCol = goalCell % columns;
            int dx = Math.abs(col - goalCol);
            int dy = Math.abs(row - goalRow);
            return 1.0 / (dx + dy + 1);
        }
    }

    /**
     * Runs the Ant Colony Optimization algorithm to find the path in the maze.
     */
    public void runACO() {
        int numAnts = 20;
        int maxIterations = 10;
        int[] bestPath = null;
        int bestPathLength = Integer.MAX_VALUE;

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            Ant[] ants = new Ant[numAnts];

            for (int i = 0; i < numAnts; i++) {
                ants[i] = new Ant(i);
                ants[i].run();
                int[] path = ants[i].getPath();
                int pathLength = ants[i].getPathLength();

                if (path[pathLength - 1] == goalCell && pathLength < bestPathLength) {
                    bestPath = path;
                    bestPathLength = pathLength;
                }
            }

            updatePheromoneLevels(ants);
//            System.out.println("Iteration: " + (iteration + 1));
//            System.out.println("Best path: " + formatPath(bestPath, bestPathLength));
//            System.out.println("Path length: " + bestPathLength);
//            System.out.println();
        }

        if (bestPath != null) {
            System.out.println("Output using ACO: " + formatPath(bestPath, bestPathLength));
        } else {
            System.out.println("No path exists.");
        }
    }

    /**
     * Updates the pheromone levels based on the paths taken by the ants.
     *
     * @param ants An array of ants.
     */
    private void updatePheromoneLevels(Ant[] ants) {
        double evaporationRate = 0.5;

        // Evaporate pheromone levels
        for (int i = 0; i < rows * columns; i++) {
            for (int j = 0; j < rows * columns; j++) {
                if (maze[i / columns][i % columns] != WALL && maze[j / columns][j % columns] != WALL) {
                    pheromoneLevels[i][j] *= (1 - evaporationRate);
                }
            }
        }

        // Deposit pheromone on the paths taken by the ants
        for (Ant ant : ants) {
            int[] path = ant.getPath();
            int pathLength = ant.getPathLength();
            double pheromone = 1.0 / pathLength;

            for (int i = 0; i < pathLength - 1; i++) {
                int fromCell = path[i];
                int toCell = path[i + 1];
                pheromoneLevels[fromCell][toCell] += pheromone;
                pheromoneLevels[toCell][fromCell] += pheromone;
            }
        }
    }
}



