/*
 * How to test? Run `mvn test -Dtest=DinicGraphTest`.
 */
package org.example.algorithms.networkflow.dinic;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

// Residual Graph
public class DinicGraph {

    private int n; // number of vertex
    private int s; // number of the source vertex
    private int t; // number of the sink vertex
    private int[] level; // stores level of a node
    private Vector<Edge>[] adj;

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

    // A DFS based function to send flow after BFS has
    // figured out that there is a possible flow and
    // constructed levels. This function called multiple
    // times for a single call of BFS.
    // flow : Current flow send by parent function call
    // start[] : To keep track of next edge to be explored.
    // start[i] stores count of edges explored
    // from i.
    // u : Current vertex
    public int sendFlow(int u, int flow, int start[]) {
        // Sink reached
        if (u == t) {
            return flow;
        }

        // Traverse all adjacent edges one -by - one.
        for (; start[u] < adj[u].size(); start[u]++) {
            // Pick next edge from adjacency list of u
            Edge e = adj[u].get(start[u]);

            if (level[e.v] == level[u] + 1 && e.flow < e.capacity) {
                // find minimum flow from u to t
                int currentFlow = Math.min(flow, e.capacity - e.flow);
                int tempFlow = sendFlow(e.v, currentFlow, start);

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
    public int computeMaxFlow() {
        // Corner case
        if (s == t) {
            return -1;
        }

        int total = 0; // Initialize result

        // Augment the flow while there is path
        // from source to sink
        while (doBFS(s, t) == true) {
            // store how many edges are visited
            // from V { 0 to V }
            int[] start = new int[n + 1];

            // while flow is not zero in graph from S to D
            int flow = sendFlow(s, Integer.MAX_VALUE, start);

            while (flow > 0) {
                // Add path flow to overall flow
                total += flow;
                flow = sendFlow(s, Integer.MAX_VALUE, start);
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
}
