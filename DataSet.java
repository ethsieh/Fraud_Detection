import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;

public class DataSet {
    private int n, m;
    private Point2D[] locations;
    private int[][] input;
    private int[] labels;

    public DataSet(String filename) {
        In datafile = new In(filename);

        n = datafile.readInt();
        m = datafile.readInt();

        locations = new Point2D[m];
        for (int i = 0; i < m; i++) {
            double x = datafile.readDouble();
            double y = datafile.readDouble();
            locations[i] = new Point2D(x, y);
        }

        labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = datafile.readInt();
        }

        input = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                input[i][j] = datafile.readInt();
            }
        }
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public Point2D[] getLocations() {
        return locations.clone();
    }

    public int[][] getInput() {
        int[][] copy = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                copy[i][j] = input[i][j];
            }
        }
        return copy;
    }

    public int[] getLabels() {
        return labels.clone();
    }

    public static void main(String[] args) { }
}

