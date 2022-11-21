/*
 * Converted the CPP code, originally found in https://www.geeksforgeeks.org/dinics-algorithm-maximum-flow/,
 * to Java code.
 * 
 * Execute `mvn compile exec:java -Dexec.mainClass=org.example.algorithms.networkflow.dinic.Graph`.
 */
package org.example.algorithms.networkflow.dinic;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

// Residual Graph
public class Graph {

    private int n; // number of vertex
    private int[] level; // stores level of a node
    private Vector<Edge>[] adj;

    public Graph(int n) {
        adj = new Vector[n];
        for (int i = 0; i < n; i++)
            adj[i] = new Vector<Edge>();
        this.n = n;
        level = new int[n];
    }

    // add edge to the graph
    public void addEdge(int u, int v, int C) {
        // Forward edge : 0 flow and C capacity
        Edge a = new Edge(v, 0, C, (int) adj[v].size());

        // Back edge : 0 flow and 0 capacity
        Edge b = new Edge(u, 0, 0, (int) adj[u].size());

        adj[u].add(a);
        adj[v].add(b); // reverse edge
    }

    // Finds if more flow can be sent from s to t.
    // Also assigns levels to nodes.
    public boolean doBFS(int s, int t) {
        for (int i = 0; i < n; i++)
            level[i] = -1;

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

    // A DFS based function to send flow after BFS has
    // figured out that there is a possible flow and
    // constructed levels. This function called multiple
    // times for a single call of BFS.
    // flow : Current flow send by parent function call
    // start[] : To keep track of next edge to be explored.
    // start[i] stores count of edges explored
    // from i.
    // u : Current vertex
    // t : Sink
    public int sendFlow(int u, int flow, int t, int start[]) {
        // Sink reached
        if (u == t)
            return flow;

        // Traverse all adjacent edges one -by - one.
        for (; start[u] < adj[u].size(); start[u]++) {
            // Pick next edge from adjacency list of u
            Edge e = adj[u].get(start[u]);

            if (level[e.v] == level[u] + 1 && e.flow < e.capacity) {
                // find minimum flow from u to t
                int currentFlow = Math.min(flow, e.capacity - e.flow);

                int tempFlow = sendFlow(e.v, currentFlow, t, start);

                // flow is greater than zero
                if (tempFlow > 0) {
                    // add flow to current edge
                    e.flow += tempFlow;

                    // subtract flow from reverse edge
                    // of current edge
                    adj[e.v].get(e.rev).flow -= tempFlow;
                    return tempFlow;
                }
            }
        }

        return 0;
    }

    // Returns maximum flow in graph
    public int computeDinicMaxflow(int s, int t) {
        // Corner case
        if (s == t)
            return -1;

        int total = 0; // Initialize result

        // Augment the flow while there is path
        // from source to sink
        while (doBFS(s, t) == true) {
            // store how many edges are visited
            // from V { 0 to V }
            int[] start = new int[n + 1];

            // while flow is not zero in graph from S to D
            int flow = sendFlow(s, Integer.MAX_VALUE, t, start);
            while (flow > 0) {

                // Add path flow to overall flow
                total += flow;
                flow = sendFlow(s, Integer.MAX_VALUE, t, start);
            }
        }

        // return maximum flow
        return total;

    }

    // Driver Code
    public static void main(String[] args) {
        Graph g = new Graph(6);
        g.addEdge(0, 1, 16);
        g.addEdge(0, 2, 13);
        g.addEdge(1, 2, 10);
        g.addEdge(1, 3, 12);
        g.addEdge(2, 1, 4);
        g.addEdge(2, 4, 14);
        g.addEdge(3, 2, 9);
        g.addEdge(3, 5, 20);
        g.addEdge(4, 3, 7);
        g.addEdge(4, 5, 4);

        // next exmp
        /*
         * g.addEdge(0, 1, 3 ); g.addEdge(0, 2, 7 ) ; g.addEdge(1, 3, 9); g.addEdge(1, 4, 9 ); g.addEdge(2, 1, 9 );
         * g.addEdge(2, 4, 9); g.addEdge(2, 5, 4); g.addEdge(3, 5, 3); g.addEdge(4, 5, 7 ); g.addEdge(0, 4, 10);
         * 
         * // next exp g.addEdge(0, 1, 10); g.addEdge(0, 2, 10); g.addEdge(1, 3, 4 ); g.addEdge(1, 4, 8 ); g.addEdge(1,
         * 2, 2 ); g.addEdge(2, 4, 9 ); g.addEdge(3, 5, 10 ); g.addEdge(4, 3, 6 ); g.addEdge(4, 5, 10 );
         */

        System.out.println("Maximum flow " + g.computeDinicMaxflow(0, 5));
    }
}

class Edge {

    Edge(int v, int flow, int capacity, int rev) {
        this.v = v;
        this.flow = flow;
        this.capacity = capacity;
        this.rev = rev;
    }

    int v; // Vertex v (or "to" vertex)
           // of a directed edge u-v. "From"
           // vertex u can be obtained using
           // index in adjacent array.

    int flow; // flow of data in edge

    int capacity; // capacity

    int rev; // To store index of reverse
             // edge in adjacency list so that
             // we can quickly find it.
}
