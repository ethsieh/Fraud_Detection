import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class BoostingAlgorithm {
    private Clustering cluster; // object that creates clusters from Point2D array
    private int[] labels; // labels of each input, either 0 or 1
    private double[] weights; // weights of each input
    private int n; // labeled transaction summaries
    private int m; // number of map locations
    private int[][] reduced; // reduced transaction summaries by cluster
    private Queue<WeakLearner> allWeakLearners; // data structure of all WeakLearners

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {

        // corner cases:

        // arguments cannot be null
        if (input == null || labels == null || locations == null)
            throw new IllegalArgumentException("Argument is null");

        // lengths of arrays must be compatible
        if (input.length != labels.length || input[0].length != locations.length)
            throw new IllegalArgumentException("Lengths are incompatible");

        // number of clusters must be within 1 to m (number of map locations)
        if (k < 1 || k > locations.length)
            throw new IllegalArgumentException("k is less than 1 or greater than m");

        // labels must be either 0 or 1
        for (int label : labels) {
            if (label != 0 && label != 1)
                throw new IllegalArgumentException("Invalid label");
        }

        // initialize instance variables
        allWeakLearners = new Queue<>();
        cluster = new Clustering(locations, k);

        // defensive copy
        this.labels = new int[labels.length];
        for (int label = 0; label < labels.length; label++) {
            this.labels[label] = labels[label];
        }

        n = input.length;
        m = input[0].length;
        reduced = new int[n][k];

        // for each transaction summary, reduce it from length m to k
        for (int i = 0; i < n; i++) {
            reduced[i] = cluster.reduceDimensions(input[i]);
        }

        // normalize all our weights, which means the entries must sum to 1
        weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = 1.0 / n;
        }

    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        // corner case: the point must be in range: 0 to n
        if (i < 0 || i >= n)
            throw new IllegalArgumentException("Point index is out of bounds");
        return weights[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {

        // each iteration creates its own WeakLearner, which is added to our queue
        WeakLearner weakLearner = new WeakLearner(reduced, weights, labels);

        double sumWeights = 0.0;
        allWeakLearners.enqueue(weakLearner);

        // if a prediction is incorrect, double the weights of that points
        for (int i = 0; i < n; i++) {
            if (weakLearner.predict(reduced[i]) != labels[i]) {
                weights[i] *= 2;
            }
            sumWeights += weights[i];
        }

        // re-normalize weights
        for (int i = 0; i < n; i++) {
            weights[i] /= sumWeights;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {

        // corner case: sample cannot be null
        // and its length must be compatible with the number of map locations
        if (sample == null || sample.length != m)
            throw new IllegalArgumentException("Invalid sample");

        int count0 = 0; // counting number of "0" predictions
        int count1 = 0; // counting number of "1" predictions
        sample = cluster.reduceDimensions(sample);

        for (WeakLearner learner : allWeakLearners) {
            int prediction = learner.predict(sample);
            if (prediction == 0) {
                count0++; // increment 0 if prediction = 0
            }
            else {
                count1++; // increment 1 if prediction = 1
            }
        }

        // return the majority; if tied, return 0
        if (count0 >= count1) return 0;
        else return 1;
    }

    // unit testing (required)
    public static void main(String[] args) {

        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int iterations = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        // train the model
        Stopwatch timer = new Stopwatch();
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput, trainingLabels,
                                                        trainingLocations, k);
        for (int t = 0; t < iterations; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.getN();

        // calculate the test data set accuracy
        double testAccuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                testAccuracy += 1;
        testAccuracy /= testing.getN();
        StdOut.println("Time elapsed: " + timer.elapsedTime());

        StdOut.println(model.weightOf(1));
        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model: " + testAccuracy);
        
    }
}
