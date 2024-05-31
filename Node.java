public class Node {
    int row;
    int col;
    int g; // Cost from start node to current node
    int h; // Heuristic value (estimated cost from current node to goal node)
    int f; // Total cost (g + h)
    Node parent; // Parent node
    private int value;
    private boolean start;
    private boolean goal;
    private boolean solid;
    private boolean checked;
    private boolean open;

    public Node(int row, int col, int value) {
        this.value = value;
        this.row = row;
        this.col = col;
        g = 0;
        h = 0;
        f = 0;
        parent = null;
        set();
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isOpen() {
        return open;
    }

    private void set() {
        if (this.value == 0) {
            setSolid();
        } else if (this.value == 2) {
            setStart();
        } else if (this.value == 3) {
            setGoal();
        }
    }

    public boolean isGoal() {
        return goal;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isStart() {
        return start;
    }

    private void setStart() {
        start = true;
    }

    private void setGoal() {
        goal = true;
    }

    private void setSolid() {
        solid = true;
    }

    public void setChecked() {
        if (solid == false && goal == false)
            checked = true;
    }

    public void setOpen() {
        open = true;
    }

    public String toString() {
        return "(" + row + "," + col + ")" + " h= " + h + " g= " + g + " f= " + f;
    }
}
