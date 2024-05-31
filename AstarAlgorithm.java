import java.util.ArrayList;
import java.util.LinkedList;

public class AstarAlgorithm {
    private int grid_cols;
    private int grid_rows;
    private int[][] maze;
    private Node startNode, goalNode, currentNode;
    Node[][] nodes;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();
    boolean goalReached = false;
    private static int steps = 0;

    public AstarAlgorithm(int rows, int cols, int[][] maze) {
        grid_rows = rows;
        grid_cols = cols;
        nodes = new Node[rows][cols];
        this.maze = maze;
        for (int i = 0; i < grid_rows; i++) {
            for (int j = 0; j < grid_cols; j++) {
                nodes[i][j] = new Node(i, j, maze[i][j]);
            }
        }
        setNodes();
        setCosts();

    }

    private void setCosts() {
        for (int i = 0; i < grid_rows; i++) {
            for (int j = 0; j < grid_cols; j++) {
                getCost(nodes[i][j]);
            }
        }
    }

    private void getCost(Node node) {
        //G
        int x = Math.abs(node.col - startNode.col);
        int y = Math.abs(node.row - startNode.row);
        node.g = x + y;
        //H
        int x1 = Math.abs(node.col - goalNode.col);
        int y1 = Math.abs(node.row - goalNode.row);
        node.h = x1 + y1;
        //f
        node.f = node.g + node.h;
    }

    private void setNodes() {
        for (int i = 0; i < grid_rows; i++) {
            for (int j = 0; j < grid_cols; j++) {
                if (nodes[i][j].isStart()) {
                    startNode = nodes[i][j];
                    currentNode = startNode;
                } else if (nodes[i][j].isGoal()) {
                    goalNode = nodes[i][j];
                }
            }
        }
    }

    public void search() {
        while (goalReached == false && steps <= Math.pow(Integer.max(grid_cols, grid_rows), 3)) {
            int row = currentNode.row;
            int col = currentNode.col;
            currentNode.setChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);
            //open up node
            if (row - 1 >= 0)
                openNode(nodes[row - 1][col]);

            //open left node
            if (col - 1 >= 0)
                openNode(nodes[row][col - 1]);
            //open right node
            if (col + 1 <= grid_cols - 1)
                openNode(nodes[row][col + 1]);
            //open down node
            if (row + 1 <= grid_rows - 1)
                openNode(nodes[row + 1][col]);
            //find best node
            int bestNodeIndex = 0;
            int bestNodef = Integer.MAX_VALUE;
            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).f < bestNodef) {
                    bestNodeIndex = i;
                    bestNodef = openList.get(i).f;
                }//if f equal then check g
                else if (openList.get(i).f == bestNodef) {
                    if (openList.get(i).g < openList.get(bestNodeIndex).g) {
                        bestNodeIndex = i;
                    }
                }
            }

            if (bestNodeIndex == 0 && openList.size() == 0) {
                System.out.println("No path exists");
                break;
            }
            //after loop set current node as best node
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                backtrack();
            }
            steps++;
        }
        if (goalReached == false && steps == Math.pow(Integer.max(grid_cols, grid_rows), 3)) {
            System.out.println("No path exists");
        }

    }

    private void backtrack() {
        LinkedList<Node> path = new LinkedList<>();
        path.add(goalNode);
        Node current = goalNode;
        while (current != startNode) {
            current = current.parent;
            path.addFirst(current);
        }
        System.out.print("output using A star: ");
        for (int i = 0; i < path.size() - 1; i++) {
            System.out.print("(" + path.get(i).row + "," + path.get(i).col + ") -> ");
        }
        System.out.println("(" + path.get(path.size() - 1).row + ", " + path.get(path.size() - 1).col + ")");
    }

    private void openNode(Node node) {
        if (node.isOpen() == false && node.isChecked() == false && node.isSolid() == false) {
            node.setOpen();
            node.parent = currentNode;
            openList.add(node);

        }
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getGoalNode() {
        return goalNode;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

}

