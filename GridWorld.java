package GridWorld;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Stack;

public class GridWorld {

    public static final char UNBLOCKED = '-';
    public static final char BLOCKED = 'X';
    public static int GRID_SIZE;
    private final char[][] grid;
    private final boolean[][] visited;

    public static record Coordinates(int row, int column) {
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Coordinates that = (Coordinates) o;
            return row == that.row && column == that.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }

    public GridWorld(char[][] grid, int GRID_SIZE) {
        this.grid = grid;
        this.GRID_SIZE = GRID_SIZE;
        visited = new boolean[0][0];
    }

    public GridWorld(int GRID_SIZE, Random random) {
        this.GRID_SIZE = GRID_SIZE;
        grid = new char[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = UNBLOCKED;
            }
        }
        visited = new boolean[GRID_SIZE][GRID_SIZE];
        int row = (int) (random.nextDouble() * GRID_SIZE);
        int column = (int) (random.nextDouble() * GRID_SIZE);
        Stack<Coordinates> traversal = new Stack<>();
        traversal.add(new Coordinates(row, column));
        visited[row][column] = true;
        while (!traversal.isEmpty()) {
            ArrayList<Coordinates> directions = new ArrayList<>();
            Coordinates current = traversal.peek();
            if (current.row - 1 >= 0 && !visited[current.row - 1][current.column])
                directions.add(new Coordinates(current.row - 1, current.column));
            if (current.row + 1 < GRID_SIZE && !visited[current.row + 1][current.column])
                directions.add(new Coordinates(current.row + 1, current.column));
            if (current.column - 1 >= 0 && !visited[current.row][current.column - 1])
                directions.add(new Coordinates(current.row, current.column - 1));
            if (current.column + 1 < GRID_SIZE && !visited[current.row][current.column + 1])
                directions.add(new Coordinates(current.row, current.column + 1));
            if (directions.size() == 0) {
                traversal.pop();
            } else {
                int value = (int) (random.nextDouble() * directions.size());
                Coordinates next = directions.get(value);
                traversal.add(next);
                visited[next.row][next.column] = true;
                if (random.nextDouble() < 0.3) {
                    grid[next.row][next.column] = BLOCKED;
                }
            }
        }
    }

    public char getStatus(int row, int column) {
        return grid[row][column];
    }

    public char setStatus(int row, int column, char c) {
        return grid[row][column] = c;
    }

    public void printGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void printGrid(Coordinates start, Coordinates end) {
        System.out.println("Grid World");
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if(i==start.row && j==start.column)
                    System.out.print("A ");
                else if (i==end.row && j==end.column)
                    System.out.print("T");
                else
                    System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printGrid(Path followPath) {
        char[][] tempGrid = new char[GRID_SIZE][GRID_SIZE];
        while (followPath != null) {
            tempGrid[followPath.row][followPath.column] = 'O';
            followPath = followPath.nextPath;
        }
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (tempGrid[i][j] == 'O')
                    System.out.print(tempGrid[i][j] + " ");
                else
                    System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printVisited_DEBUG() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (!visited[i][j])
                    System.out.print(visited[i][j] + " " + i + " " + j + "\t");
            }
            System.out.println();
        }
    }

}