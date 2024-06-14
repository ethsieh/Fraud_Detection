Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */
We began by creating a complete edge-weighted graph using the EdgeWeightedGraph
class. We ensured that m vertices were assigned to represent each location on
the map, connecting them with edges weighted according to the Euclidean distance
between the points, where each vertex was uniquely identified by a number
ranging from 0 to m−1.

After constructing the graph, we made an MST using Kruskal's algorithm.
This enabled us to identify the smallest set of edges required to connect all
vertices while minimizing the total edge weight.

Once the minimum spanning tree was determined, we focused on the m−k edges with
the lowest weight from the spanning tree. We found this by sorting the edge weights
(by storing them in an ArrayList). With these selected edges, we crafted
a new graph known as the cluster graph. In this graph, each cluster corresponded
to a connected component, formed by the selected edges. Given that the cluster
graph contained exactly k connected components, each cluster was distinctly
represented by one of these components.

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */
The WeakLearner constructor in Java initializes a decision stump model by analyzing
input data, weights, and labels. It first validates the input arguments, ensuring
none are null and that their lengths match appropriately. It also checks that the
weights are non-negative and that the labels are either 0 or 1. Then, it iterates
over each dimension predictor and sorts input indices based on the current dimension.
For each dimension and sign combination, it calculates the weighted sum of correctly
classified inputs for various split values. It updates the parameters
(dimension predictor, value predictor, and sign predictor) based on the split
value that maximizes the sum of correctly classified inputs weighted by their respective
weights. This iterative process ensures the decision stump is trained to make
predictions that best separate the input data based on the specified criteria.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------
      20        500         0.9625              1.027
      20        1000        0.9625              2.044
      10        1000        0.9125              1.197
      1         1000        0.675               0.257
      11        1000        0.9375              1.258
      21        1000        0.9875              1.981
      21        5000        1.0                 8.61
      10        5000        0.9125              4.175
      1         5000        0.65                0.644
      1         1           0.7625              0.036
      21        1           0.625               0.035
      5         1000        0.7625              0.777
      21        6000        1.0                 9.987

/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */
k = 21, T = 6000
Training accuracy = 1.0
Test accuracy 1.0
Time: 9.987 seconds

Our stategy was basically trial and error. We started off big with the max number
of clusters and a large T, and it gave a very high test accuracy and training accuracy.
We then increased T, and we ended up with a testing accuracy of 1.0 and a training
accuracy of 1.0. Therefore, k = 21, and T = 5000 maximizes the test data set accuracy
while running under 10 seconds.

A small value of T leads the low test accuracy because in order to train a weak
learner, or in this case, boost the algorithm well, you must run many iterations so
that the algorithm can more accurately determine the prediction of the learner.

A k that is too small leads to low test accuracy because fewer clusters can lead
to underfitting, where clusters formed do not adequately capture the underlying
structure of the data. In contrast, although our data shows that k is 21, which
is the number of points, if k is too large, the algorithm creates clusters that
are too specific and capture noise in the data instead of actual
patterns.

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
We were not able to get stump_5.txt to output the correct values, and we determined
that it was due to how we sorted the weights in the optimzed solutions (constructor).

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
