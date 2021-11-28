package GridWorld;

public class Path {
    int row;
    int column;
    Path nextPath;
    int expandedCells;

    public Path(int row, int column, Path nextPath) {
        this.row = row;
        this.column = column;
        this.nextPath = nextPath;
    }

    public Path(int row, int column) {
        this.row = row;
        this.column = column;
    }

}
