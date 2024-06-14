import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Clustering {
    private CC connectedComp; // each cluster is given by a connected component
    private int clusters; // number of clusters
    private int m; // length of locations

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {

        // corner cases when arguments are null or k is out of range
        if (locations == null) throw new IllegalArgumentException("Locations is null");
        if (k > locations.length || k < 1)
            throw new IllegalArgumentException("k is less than 1 or greater than m");

        clusters = k;
        double distance;
        m = locations.length;

        // create edge weighted graph with m vertices and an edge connecting
        // pair of locations, weighted according to Euclidean distance between points
        EdgeWeightedGraph graph = new EdgeWeightedGraph(m);
        for (int i = 0; i < m - 1; i++) {
            for (int j = i + 1; j < m; j++) {
                distance = locations[i].distanceTo(locations[j]);
                Edge edge = new Edge(i, j, distance);
                graph.addEdge(edge);
            }
        }

        // compute minimum spanning tree of the graph
        KruskalMST graphMST = new KruskalMST(graph);

        // sort edges to consider only the m-k edges with lowest weight of
        // the spanning tree
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (Edge e : graphMST.edges()) {
            edges.add(e);
        }
        Collections.sort(edges);

        // create new graph with m-k edges with lowest weight
        EdgeWeightedGraph clusterGraph = new EdgeWeightedGraph(m);
        for (int i = 0; i < m - k; i++) {
            clusterGraph.addEdge(edges.get(i));
        }

        // each cluster is given by a connected component in this graph
        connectedComp = new CC(clusterGraph);
    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        // corner case for when point is out of bounds
        if (i < 0 || i > m - 1)
            throw new IllegalArgumentException("point is out of bounds");
        return connectedComp.id(i);
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        // corner case for when input is null or has incompatible length
        if (input == null || input.length != m)
            throw new IllegalArgumentException("Input length should be m");

        // create new array with the sums of each cluster
        int[] reduced = new int[clusters];
        for (int i = 0; i < input.length; i++) {
            int cluster = clusterOf(i);
            reduced[cluster] += input[i];
        }
        return reduced;
    }

    // unit testing (required)
    public static void main(String[] args) {

        // take in command-line arguments: file name and number of clusters
        String fileName = args[0];
        int k = Integer.parseInt(args[1]);

        // read file
        In readFile = new In(fileName);

        int m = readFile.readInt(); // number of locations
        Point2D[] locations = new Point2D[m];

        // read file until it is empty
        int index = 0;
        while (!readFile.isEmpty()) {
            double x = readFile.readDouble(); // x-coordinate
            double y = readFile.readDouble(); // y-coordinate

            Point2D point = new Point2D(x, y);
            locations[index] = point; // put into locations array
            index++;
        }

        // given transaction summary
        int[] summary =
                { 5, 6, 7, 0, 6, 7, 5, 6, 7, 0, 6, 7, 0, 6, 7, 0, 6, 7, 0, 6, 7 };

        Clustering c1 = new Clustering(locations, k);
        StdOut.println(c1.clusterOf(2)); // should print 1

        // reduced should print 5, 26, 24, 39, 7
        StdOut.println(
                Arrays.toString(c1.reduceDimensions(summary)));
    }
}
