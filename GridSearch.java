package GridWorld;

import java.util.PriorityQueue;
import static GridWorld.GridWorld.Coordinates;
import static GridWorld.GridWorld.GRID_SIZE;

public class GridSearch {

    private static class Node {
        public int fCost, gCost, hCost;
        public Node previous;
        public Coordinates position;
        public boolean isBlocked;
        public boolean visited;

        public Node(int gCost, int hCost, Coordinates position, Node previous) {
            this.fCost = gCost + hCost;
            this.gCost = gCost;
            this.hCost = hCost;
            this.position = position;
            this.previous = previous;
            visited = false;
        }
    }

    private Node[][] pathGrid = new Node[GridWorld.GRID_SIZE][GridWorld.GRID_SIZE];
    public boolean[][] blocked = new boolean[GridWorld.GRID_SIZE][GridWorld.GRID_SIZE];
    public int[][] gCost = new int[GridWorld.GRID_SIZE][GridWorld.GRID_SIZE];
    public boolean adaptive;
    public boolean reverseHCost;

    public GridSearch(boolean adaptive, boolean reverseHCost) {
        this.adaptive = adaptive;
        this.reverseHCost = reverseHCost;
    }

    private void processNode(PriorityQueue<Node> queue, Node current, int row, int column, Coordinates end) {
        Node node = pathGrid[row][column];
        if (node == null) {
            if (blocked[row][column])
                pathGrid[row][column] = new Node(0, 0, new Coordinates(row, column), null);
            if (!blocked[row][column]) {
                Coordinates coordinates = new Coordinates(row, column);
                pathGrid[row][column] = new Node(current.gCost + 1, findManhattanDistance(coordinates, end),
                        coordinates, current);
                gCost[row][column] = current.gCost + 1;
                queue.add(pathGrid[row][column]);
            }
        } else if (!blocked[row][column] && !node.visited && node.gCost > current.gCost + 1) {
            node.gCost = current.gCost + 1;
            gCost[row][column] = current.gCost + 1;
            node.fCost = node.gCost + node.hCost;
            node.previous = current;
        }
    }

    public Path searchGrid(Coordinates start, Coordinates end) {
        pathGrid = new Node[GridWorld.GRID_SIZE][GridWorld.GRID_SIZE];
        PriorityQueue<Node> queue = new PriorityQueue<>(10, (node1, node2) -> {
            int comparison = Integer.compare(node1.fCost, node2.fCost);
            if (comparison == 0) {
                if(reverseHCost){
                    return Integer.compare(node2.hCost, node1.hCost);
                }
                return Integer.compare(node1.hCost, node2.hCost);
            }
            return comparison;
        });
        gCost[start.row()][start.column()] = 0;
        Node startNode = new Node(0, findManhattanDistance(start, end), start, null); // this assumes start and
                                                                                      // end are different and
                                                                                      // that start is unblocked
        queue.add(startNode);
        pathGrid[start.row()][start.column()] = startNode;
        boolean pathFound = false;
        int expandedCells = 0;
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            expandedCells++;
            if (current.position.equals(end)) {
                pathFound = true;
                break;
            }
            current.visited = true;
            if (current.position.row() > 0)
                processNode(queue, current, current.position.row() - 1, current.position.column(), end);
            if (current.position.column() > 0)
                processNode(queue, current, current.position.row(), current.position.column() - 1, end);
            if (current.position.row() < GRID_SIZE - 1)
                processNode(queue, current, current.position.row() + 1, current.position.column(), end);
            if (current.position.column() < GRID_SIZE - 1)
                processNode(queue, current, current.position.row(), current.position.column() + 1, end);
        }

        if (pathFound) {
            //System.out.println("Path found");
            Node current = pathGrid[end.row()][end.column()];
            Path path = null;
            while (current != null) {
                path = new Path(current.position.row(), current.position.column(), path);
                current = current.previous;
            }
            path.expandedCells = expandedCells;
            return path;
        } else {
            // System.out.println("No Path found");
            return new Path(-1, -1);
        }
    }

    public int findManhattanDistance(Coordinates start, Coordinates end) {
        int mahattan = Math.abs(start.row() - end.row()) + Math.abs(start.column() - end.column());
        if (adaptive) {
            int newH = gCost[end.row()][end.column()] - gCost[start.row()][start.column()];
            if (newH > mahattan)
                return newH;
        }
        return mahattan;
    }

    public void setBlockedNode(int row, int column) {
        blocked[row][column] = true;
//        System.out.println("Blocked "+row+","+column);
    }

    public void printCurrentGrid() {
        for (int i = 0; i < pathGrid.length; i++) {
            for (int j = 0; j < pathGrid[0].length; j++) {
                if (pathGrid[i][j] == null)
                    System.out.print(". ");
                else if (pathGrid[i][j].isBlocked)
                    System.out.print("X ");
                else
                    System.out.print("- ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
