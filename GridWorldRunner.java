package GridWorld;

import GridWorld.GridWorld.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.jar.JarOutputStream;

public class GridWorldRunner {

    public static void generateRandomCoordinates(GridWorld gridWorld, int[] values, Random random) {
        do {
            values[0] = (int) (random.nextDouble() * GridWorld.GRID_SIZE);
            values[1] = (int) (random.nextDouble() * GridWorld.GRID_SIZE);
        } while (gridWorld.getStatus(values[0], values[1]) != GridWorld.BLOCKED);
        do {
            values[2] = (int) (random.nextDouble() * GridWorld.GRID_SIZE);
            values[3] = (int) (random.nextDouble() * GridWorld.GRID_SIZE);
        } while (gridWorld.getStatus(values[0], values[1]) != GridWorld.BLOCKED && (values[0] != values[2] || values[1] != values[3]));
    }

    public static void main(String[] args) {
         ArrayList<GridWorld> gridWorlds = new ArrayList<>();
         long totalTime = 0;
         int totalNodesExpanded = 0;
         for (int i = 0; i < 50; i++) {
             GridWorld gridWorld = new GridWorld(101, new Random(0));
             gridWorlds.add(gridWorld);
             int[] values = new int[4];
             Random random = new Random(i);
             generateRandomCoordinates(gridWorld, values, random);
//             System.out.println(Arrays.toString(values));
             long beginning = System.nanoTime();
//             totalNodesExpanded += RepeatedBackwardA(gridWorld, new Coordinates(values[0], values[1]), new Coordinates(values[2], values[3]));
//             totalNodesExpanded += RepeatedForwardA(gridWorld, new Coordinates(values[0], values[1]), new Coordinates(values[2], values[3]), true);
             totalNodesExpanded += RepeatedForwardA(gridWorld, new Coordinates(values[0], values[1]), new Coordinates(values[2], values[3]), true, false);
             long ending = System.nanoTime();
//             gridWorld.printGrid();
             totalTime += (ending - beginning);
//             System.out.println("Elapsed Time: " + (ending - beginning) / 1E6 + " milliseconds | Total time: " + totalTime / 1E6 + " milliseconds" + "\n");
         }

        System.out.println("Total Expanded Cell\t" + totalNodesExpanded);
 }

//    public static void main(String[] args) {
//        char[][] grid = { { '-', '-', 'X', '-', '-' }, { '-', '-', 'X', '-', '-' }, { '-', '-', 'X', 'X', '-' },
//                { '-', '-', 'X', 'X', '-' }, { '-', '-', '-', 'X', '-' } };
//        //GridWorld gridWorld = new GridWorld(grid, 5);
//
//        GridWorld gridWorld = new GridWorld(10, new Random());
//
//        //int timeForward = RepeatedForwardA(gridWorld,  new Coordinates(4, 2), new Coordinates(4, 4));
//        int timeBackward = RepeatedBackwardA(gridWorld,  new Coordinates(4, 1), new Coordinates(4, 4));
//        //System.out.println("Forward Search = " + timeForward);
//        System.out.println("Backward Search = " + timeBackward);
//
//    }

    static int RepeatedForwardA(GridWorld gridWorld, Coordinates start, Coordinates target, boolean adaptive, boolean reverseHCost) {
        GridSearch gridSearch = new GridSearch(adaptive,reverseHCost);
//        gridWorld.printGrid(start, target);
//        System.out.println("Running Repeated Forward A* Search");
        boolean targetReached = false;
        int timeStep = 1;
        exploreNode(start, gridWorld, gridSearch);
        int expandedCells = 0;
        while (!targetReached) {
            Path path = gridSearch.searchGrid(start, target);
            expandedCells += path.expandedCells;
//            printList(path);
//            System.out.println("Time Step " + timeStep);
            timeStep++;
            Path starting = path;
            boolean[][] block = new boolean[gridWorld.GRID_SIZE][gridWorld.GRID_SIZE];
            System.arraycopy(gridSearch.blocked, 0, block, 0, gridWorld.GRID_SIZE);
            Path next = path.nextPath;

            if (path.row == target.row() && path.column == target.column()) {
                targetReached = true;
//                System.out.println("Target Reached");
//                printGrid(starting, start, target, block, targetReached, gridWorld.GRID_SIZE);
            } else if (next == null) {
//                printGrid(starting, start, target, block, targetReached, gridWorld.GRID_SIZE);
//                System.out.println("Cannot Reach Target");
                System.out.println( expandedCells);
                return expandedCells;
            } 
            else {
                start = new Coordinates(next.row, next.column);
                exploreNode(start, gridWorld, gridSearch);
//                printGrid(starting, start, target, block, targetReached, gridWorld.GRID_SIZE);
            }

        }
        System.out.println( expandedCells);
        return expandedCells;

    }

    static int RepeatedBackwardA(GridWorld gridWorld, Coordinates start, Coordinates target) {
        GridSearch gridSearch = new GridSearch(false, false);
//        gridWorld.printGrid(start, target);
//        System.out.println("Running Repeated Backward A* Search");
        boolean targetReached = false;
        int timeStep = 1;
        int expandedCells = 0;
        exploreNode(start, gridWorld, gridSearch);
        while (!targetReached) {
            Path path = gridSearch.searchGrid(target, start);
//            System.out.println("Time Step " + timeStep);
            expandedCells += path.expandedCells;
            timeStep++;
            Path prev = null;
            Path current = path;
            Path n = null;
            while (current != null) {
                n = current.nextPath;
                current.nextPath = prev;
                prev = current;
                current = n;
            }
            path = prev;

            boolean[][] block = new boolean[gridWorld.GRID_SIZE][gridWorld.GRID_SIZE];
            System.arraycopy(gridSearch.blocked, 0, block, 0, gridWorld.GRID_SIZE);
            Path starting = path;
            Path next = path.nextPath;
            if (path.row == target.row() && path.column == target.column()) {
                targetReached = true;
//                System.out.println("Target Reached");
//                printGrid(starting, start, target, block, targetReached, gridWorld.GRID_SIZE);
            } else if (next == null) {
//                printGrid(starting, start, target, block, targetReached, gridWorld.GRID_SIZE);
//                System.out.println("Cannot Reach Target");
                System.out.println(expandedCells);
                return expandedCells;
            }
            else {
                start = new Coordinates(next.row, next.column);
                exploreNode(start, gridWorld, gridSearch);
//                printGrid(starting, start, target, block, targetReached, gridWorld.GRID_SIZE);
            }

        }
        System.out.println(expandedCells);
        return expandedCells;

    }

    static void exploreNode(Coordinates start, GridWorld gridWorld, GridSearch gridSearch) {
        if (start.row() > 0 && gridWorld.getStatus(start.row() - 1, start.column()) == 'X')
            gridSearch.setBlockedNode(start.row() - 1, start.column());
        if (start.column() > 0 && gridWorld.getStatus(start.row(), start.column() - 1) == 'X')
            gridSearch.setBlockedNode(start.row(), start.column() - 1);
        if (start.row() < gridWorld.GRID_SIZE - 1 && gridWorld.getStatus(start.row() + 1, start.column()) == 'X')
            gridSearch.setBlockedNode(start.row() + 1, start.column());
        if (start.column() < gridWorld.GRID_SIZE - 1 && gridWorld.getStatus(start.row(), start.column() + 1) == 'X')
            gridSearch.setBlockedNode(start.row(), start.column() + 1);
    }

    static void printList(Path node) {
        while (node != null) {
            System.out.print("[" + node.row + "," + node.column + "] ->");
            node = node.nextPath;
        }
        System.out.println();
    }

    static void printGrid(Path followPath, Coordinates start, Coordinates end, boolean[][] blocked, boolean target,
            int GRID_SIZE) {
        char[][] tempGrid = new char[GRID_SIZE][GRID_SIZE];
        char[][] tempGrid2 = new char[GRID_SIZE][GRID_SIZE];
        boolean stop = false;
        tempGrid[followPath.row][followPath.column] = 'A';
        tempGrid2[followPath.row][followPath.column] = 'A';
        if (!target && start.row() == followPath.row && start.column() == followPath.column) {
            stop = true;
        }
        followPath = followPath.nextPath;
        while (followPath != null) {
            tempGrid[followPath.row][followPath.column] = '*';
            if (!stop)
                tempGrid2[followPath.row][followPath.column] = 'A';
            if (start.row() == followPath.row && start.column() == followPath.column) {
                stop = true;
            }

            followPath = followPath.nextPath;
        }
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (tempGrid[i][j] == 'A')
                    System.out.print(tempGrid[i][j] + " ");
                else if (i == end.row() && j == end.column())
                    System.out.print("T");
                else if (tempGrid[i][j] == '*')
                    System.out.print(tempGrid[i][j] + " ");
                else if (blocked[i][j])
                    System.out.print("X ");
                else
                    System.out.print("- ");
            }
            System.out.print("\t");
            // for (int j = 0; j < 5; j++) {
            //     if (tempGrid2[i][j] == 'A')
            //         System.out.print(tempGrid2[i][j] + " ");
            //     else if (i==end.row() && j==end.column())
            //         System.out.print("T");
            //     else if(blocked[i][j])
            //         System.out.print("X ");
            //     else
            //         System.out.print("- ");
            // }
            System.out.println();
        }

    }

    // public static void main(String[] args) {
    // GridWorld gridWorld = new GridWorld();
    // gridWorld.printGrid();
    // GridSearch gridSearch = new GridSearch(gridWorld, new Coordinates(0, 0), new
    // Coordinates(9, 9));
    // gridSearch.searchGrid();
    // }

}
