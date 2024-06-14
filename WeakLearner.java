import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class WeakLearner {
    private int dimensionPredictor; // represents non-parallel dimension of input space
    private int valuePredictor; // where input space is partitioned
    private int signPredictor; // which direction to partition space
    private int k; // cluster size

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        // check if any argument is null
        if (input == null || weights == null || labels == null) {
            throw new IllegalArgumentException(
                    "Input, weights, or labels cannot be null");
        }

        // check if the length of input, weights, and labels are incompatible
        if (input.length != weights.length || input.length != labels.length) {
            throw new IllegalArgumentException(
                    "Lengths of input, weights, and labels are incompatible");
        }

        // check if the values of weights are not all non-negative
        for (double weight : weights) {
            if (weight < 0) {
                throw new IllegalArgumentException(
                        "Weights must be non-negative");
            }
        }

        // check if the values of labels are indeed either 0 or 1
        for (int label : labels) {
            if (label != 0 && label != 1) {
                throw new IllegalArgumentException(
                        "Labels must be 0 or 1");
            }
        }

        int n = input.length;
        k = input[0].length;

        // initialize the best weighted correctly classified inputs
        double bestWeightedCorrect = -1;

        // iterate over each dimension predictor
        for (int dim = 0; dim < k; dim++) {
            // sort input indices based on the current dimension
            Indices[] indices = new Indices[input.length];
            for (int i = 0; i < input.length; i++) {
                indices[i] = new Indices(i, input[i][dim]);
            }
            Arrays.sort(indices);

            // iterate through each sign
            for (int sign = 0; sign <= 1; sign++) {
                // keep track of the weighted correctly inputs
                double weightedCorrect = 0.0;

                // first threshold
                for (int i = 0; i < n; i++) {
                    int idx = indices[i].index;
                    if (labels[idx] != sign) weightedCorrect += weights[idx];
                }

                // calculate the weighted sum of correctly classified
                // inputs for each split value
                for (int i = 0; i < n; i++) {
                    int idx = indices[i].index;
                    double weight = weights[idx];

                    // increase weight if correct
                    if (labels[idx] == sign) {
                        weightedCorrect += weight;
                    }

                    // decrease weight if incorrect
                    else {
                        weightedCorrect -= weight;
                    }

                    // consider when multiple points are on the same line
                    if ((i + 1 < input.length) &&
                            (indices[i].val == indices[i + 1].val))
                        continue;

                    // update if coordinate value changes
                    if (weightedCorrect > bestWeightedCorrect) {
                        bestWeightedCorrect = weightedCorrect;
                        dimensionPredictor = dim;
                        valuePredictor = indices[i].val;
                        signPredictor = sign;
                    }
                }

            }
        }
    }

    // object that compares values
    private static class Indices implements Comparable<Indices> {
        private int index; // represents the index of the value in the input array
        private int val; // represents the value in the input array

        // initializes index and value
        public Indices(int index, int val) {
            this.index = index;
            this.val = val;
        }

        // compares the values between two Indices and returns an int based on
        // the result of the compare method
        public int compareTo(Indices that) {
            return Double.compare(val, that.val);
        }
    }


    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        // corner case for when sample is null or incompatible
        if (sample == null || sample.length != k)
            throw new IllegalArgumentException(
                    "Sample length is incompatible or null");
        if (signPredictor == 0) {
            if (sample[dimensionPredictor] <= valuePredictor) return 0;
            else return 1;
        }

        // reverse prediction if signPredictor == 1
        else {
            if (sample[dimensionPredictor] <= valuePredictor) return 1;
            else return 0;
        }
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dimensionPredictor;
    }

    // return the value the learner uses to separate the data
    public int valuePredictor() {
        return valuePredictor;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return signPredictor;
    }

    // unit testing (required)
    public static void main(String[] args) {

        // example: stump_5.txt
        In datafile = new In(args[0]);

        int n = datafile.readInt();
        int k = datafile.readInt();

        // populate inputs[][]
        int[][] input = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                input[i][j] = datafile.readInt();
            }
        }

        // populates labels[]
        int[] labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = datafile.readInt();
        }

        // populate weights[]
        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = datafile.readDouble();
        }

        WeakLearner weakLearner = new WeakLearner(input, weights, labels);
        StdOut.println(weakLearner.predict(input[0])); // test predict method

        // should return vp = 1, dp = 1, sp = 0
        StdOut.printf("vp = %d, dp = %d, sp = %d\n", weakLearner.valuePredictor(),
                      weakLearner.dimensionPredictor(), weakLearner.signPredictor());

    }
}
