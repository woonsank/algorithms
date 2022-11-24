/*
 * NOTICE: This class was converted to a Java implementation from the C++ source
 *         originally found in https://www.geeksforgeeks.org/dinics-algorithm-maximum-flow/,
 *         with the following minor improvements:
 *           - convert the original C++ code to Java code ofc.
 *           - move the driver main function to its dedicated JUnit test code.
 *           - add the test cases with presentation purpose graph diagrams in the src/test/resources/...
 *           - cleanups mostly about naming and following popular Java coding style conventions.
 * 
 * HOW TO TEST? Run `mvn test -Dtest=DinicGraphTest`.
 */
package org.example.algorithms.networkflow.dinic;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 * Residual/layered graph in Dinic's algorithm.
 */
public class DinicGraph {

    private int n; // number of vertex
    private int s; // number of the source vertex
    private int t; // number of the sink vertex
    private int[] level; // stores level of a node
    private Vector<Edge>[] adj;

    /**
     * Constructor.
     * @param n number of vertex
     * @param s number of the source vertex
     * @param t number of the sink vertex
     */
    public DinicGraph(int n, int s, int t) {
        adj = new Vector[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new Vector<Edge>();
        }
        this.n = n;
        this.s = s;
        this.t = t;
        level = new int[n];
    }

    /**
     * Add an edge, from the vertex {@code u} and to the vertex {@code v}.
     * @param u the number of vertex incident with the tail of the edge
     * @param v the number of vertex incident with the head of the edge
     * @param capacity the capacity of the edge
     */
    public void addEdge(int u, int v, int capacity) {
        // Forward edge : 0 flow and C capacity
        Edge a = new Edge(v, 0, capacity, adj[v].size());
        // Back edge : 0 flow and 0 capacity
        Edge b = new Edge(u, 0, 0, adj[u].size());
        adj[u].add(a);
        adj[v].add(b); // reverse edge
    }

    /**
     * Reset the levels of the layers on each node by BFS and check whether or not more flow can be
     * sent from s to t, and return {@code true} if more flow can be sent from {@code s} to {@code t},
     * or {@code false} otherwise.
     * <P>
     * Note that the {@code level} will contain only _available_ nodes.
     * @param s number of the source vertex
     * @param t number of the sink vertex
     * @return {@code true} if more flow can be sent from {@code s} to {@code t}, or {@code false} otherwise
     */
    public boolean resetLayerLevelsByBFS(int s, int t) {
        for (int i = 0; i < n; i++) {
            level[i] = -1;
        }

        level[s] = 0; // Level of source vertex

        // Create a queue, enqueue source vertex
        // and mark source vertex as visited here
        // level[] array works as visited array also.
        Queue<Integer> q = new LinkedList<>();
        q.add(s);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (Edge e : adj[u]) {
                if (level[e.v] < 0 && e.flow < e.capacity) {
                    // Level of current vertex is,
                    // level of parent + 1
                    level[e.v] = level[u] + 1;
                    q.add(e.v);
                }
            }
        }

        // IF we can not reach to the sink we
        // return false else true
        return level[t] < 0 ? false : true;
    }

    /**
     * A DFS based function to send flow after BFS has
     * figured out that there is a possible flow and
     * constructed levels. This function called multiple
     * times for a single call of BFS.
     * @param u current vertex
     * @param flow current flow send by parent function call
     * @param edgeIndexes to keep track of the index of next edge to be explored from {@code i}
     * @return the flow through the s-t path
     */
    public int sendFlowByDFS(int u, int flow, int edgeIndexes[]) {
        // Sink reached
        if (u == t) {
            return flow;
        }

        // Traverse all adjacent edges one by one.
        for (; edgeIndexes[u] < adj[u].size(); edgeIndexes[u]++) {
            // Pick next edge from adjacency list of u
            Edge e = adj[u].get(edgeIndexes[u]);

            if (level[e.v] == level[u] + 1 && e.flow < e.capacity) {
                // find minimum flow from u to t
                int currentFlow = Math.min(flow, e.capacity - e.flow);
                int tempFlow = sendFlowByDFS(e.v, currentFlow, edgeIndexes);

                // flow is greater than zero
                if (tempFlow > 0) {
                    // add flow to current edge
                    e.flow += tempFlow;
                    // subtract flow from reverse edge
                    // of current edge
                    adj[e.v].get(e.reverseEdgeIndex).flow -= tempFlow;
                    return tempFlow;
                }
            }
        }

        return 0;
    }

    /**
     * Compute and returns the maximum flow in graph.
     * @return computed maximum flow in graph
     */
    public int computeMaxFlow() {
        // Corner case
        if (s == t) {
            return -1;
        }

        int total = 0; // Initialize result

        // Augment the flow while there is path from source to sink
        while (resetLayerLevelsByBFS(s, t) == true) {
            // store how many edges are visited from v { 0 to d(v) }
            int[] edgeIndexes = new int[n + 1];
            int flow;
            // while flow is not zero in graph from source to sink
            while ((flow = sendFlowByDFS(s, Integer.MAX_VALUE, edgeIndexes)) > 0) {
                // Add path flow to overall flow
                total += flow;
            }
        }

        // return maximum flow
        return total;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(
                "DinicGraph (n=" + n + ", source=" + s + ", sink=" + t + ") with arcs:");
        for (int i = 0; i < n; i++) {
            for (Edge edge : adj[i]) {
                if (edge.capacity > 0) {
                    sb.append("\n\t").append(i)
                            .append(" -> ")
                            .append(edge.v)
                            .append(" (" + Math.max(0, edge.flow) + "/" + edge.capacity + ")");
                }
            }
        }
        return sb.toString();
    }

    class Edge {

        Edge(int v, int flow, int capacity, int reverseEdgeIndex) {
            this.v = v;
            this.flow = flow;
            this.capacity = capacity;
            this.reverseEdgeIndex = reverseEdgeIndex;
        }

        int v; // Vertex v (or "to" vertex)
               // of a directed edge u-v. "From"
               // vertex u can be obtained using
               // index in adjacent array.

        int flow; // flow of data in edge

        int capacity; // capacity

        int reverseEdgeIndex; // To store index of reverse
                              // edge in adjacency list so that
                              // we can quickly find it.
    }
}
