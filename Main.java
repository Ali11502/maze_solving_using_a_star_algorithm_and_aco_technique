import java.util.Scanner;

public class Main {
//    public void readMaze() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter number of rows: ");
//        rows = scanner.nextInt();
//        System.out.print("Enter number of columns: ");
//        columns = scanner.nextInt();
//        maze = new int[rows][columns];
//        pheromoneLevels = new double[rows * columns][rows * columns];
//
//        System.out.println("Enter the maze (0 = wall, 1 = open, 2 = start, 3 = goal):");
//        for (int i = 0; i < rows; i++) {
//            String line = scanner.next();
//            for (int j = 0; j < columns; j++) {
//                maze[i][j] = Character.getNumericValue(line.charAt(j));
//                if (maze[i][j] == START) {
//                    startCell = i * columns + j;
//                } else if (maze[i][j] == GOAL) {
//                    goalCell = i * columns + j;
//                }
//            }
//        }
//
//        initializePheromoneLevels();
//    }

    public static void main(String[] args) {
        int[][] maze;
        Scanner scanner = new Scanner(System.in);

        System.out.println("key: 0=solid, 1=open,2=start, 3=goal, ");
        int rows, cols;
        System.out.print("enter rows number: ");
        rows = scanner.nextInt();
        System.out.print(" enter columns number: ");
        cols = scanner.nextInt();
        maze = new int[rows][cols];
        System.out.println("Paste 2d array ");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = scanner.nextInt();
            }
        }
        int choice;
        long starttime, endtime;
        while (true) {
            System.out.print("to find path press 0 (A star algorithm is used) " + '\n' +
                    "or to find optimised path press  1 (ACO is used) " + '\n' +
                    "or to find both together press 2 " + '\n' + " press 3 to exit: ");
            choice = scanner.nextInt();
            if (choice == 0) {
                AstarAlgorithm as = new AstarAlgorithm(rows, cols, maze);
                starttime = System.nanoTime();
                as.search();
                endtime = System.nanoTime();
                System.out.println("time taken by A star= " + (endtime - starttime) + " nanoseconds");
            } else if (choice == 1) {
                ACOMazeSolver aco = new ACOMazeSolver(rows, cols, maze);
                starttime = System.nanoTime();
                aco.runACO();
                endtime = System.nanoTime();
                System.out.println("time taken by aco = " + (endtime - starttime) + " nanoseconds");
            } else if (choice == 2) {
                AstarAlgorithm as = new AstarAlgorithm(rows, cols, maze);
                starttime = System.nanoTime();
                as.search();
                endtime = System.nanoTime();
                System.out.println("time taken by A star= " + (endtime - starttime) + " nanoseconds");

                ACOMazeSolver aco = new ACOMazeSolver(rows, cols, maze);
                starttime = System.nanoTime();
                aco.runACO();
                endtime = System.nanoTime();
                System.out.println("time taken by aco = " + (endtime - starttime) + " nanoseconds");
            } else if (choice == 3) break;

        }
    }
}
