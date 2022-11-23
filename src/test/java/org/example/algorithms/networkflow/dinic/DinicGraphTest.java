package org.example.algorithms.networkflow.dinic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DinicGraphTest {

    private static final int EXPECTED_FLOW_OF_G1 = 23;
    private static final int EXPECTED_FLOW_OF_G2 = 14;
    private static final int EXPECTED_FLOW_OF_G3 = 19;

    private DinicGraph g1;
    private DinicGraph g2;
    private DinicGraph g3;

    @BeforeEach
    public void setUp() {
        g1 = new DinicGraph(6, 0, 5);
        g1.addEdge(0, 1, 16);
        g1.addEdge(0, 2, 13);
        g1.addEdge(1, 2, 10);
        g1.addEdge(1, 3, 12);
        g1.addEdge(2, 1, 4);
        g1.addEdge(2, 4, 14);
        g1.addEdge(3, 2, 9);
        g1.addEdge(3, 5, 20);
        g1.addEdge(4, 3, 7);
        g1.addEdge(4, 5, 4);

        g2 = new DinicGraph(6, 0, 5);
        g2.addEdge(0, 1, 3);
        g2.addEdge(0, 2, 7);
        g2.addEdge(1, 3, 9);
        g2.addEdge(1, 4, 9);
        g2.addEdge(2, 1, 9);
        g2.addEdge(2, 4, 9);
        g2.addEdge(2, 5, 4);
        g2.addEdge(3, 5, 3);
        g2.addEdge(4, 5, 7);
        g2.addEdge(0, 4, 10);

        g3 = new DinicGraph(6, 0, 5);
        g3.addEdge(0, 1, 10);
        g3.addEdge(0, 2, 10);
        g3.addEdge(1, 3, 4);
        g3.addEdge(1, 4, 8);
        g3.addEdge(1, 2, 2);
        g3.addEdge(2, 4, 9);
        g3.addEdge(3, 5, 10);
        g3.addEdge(4, 3, 6);
        g3.addEdge(4, 5, 10);
    }

    @Test
    public void testG1() {
        System.out.println("Graph g1 (before computed): " + g1);
        int maxFlow = g1.computeMaxFlow();
        System.out.println("Graph g1 (after computed): " + g1);
        System.out.println("Computed max flow in g1: " + maxFlow);
        Assertions.assertEquals(EXPECTED_FLOW_OF_G1, maxFlow);
    }

    @Test
    public void testG2() {
        System.out.println("Graph g2 (before computed): " + g2);
        int maxFlow = g2.computeMaxFlow();
        System.out.println("Graph g2 (after computed): " + g2);
        System.out.println("Computed max flow in g2: " + maxFlow);
        Assertions.assertEquals(EXPECTED_FLOW_OF_G2, maxFlow);
    }

    @Test
    public void testG3() {
        System.out.println("Graph g3 (before computed): " + g3);
        int maxFlow = g3.computeMaxFlow();
        System.out.println("Graph g3 (after computed): " + g3);
        System.out.println("Computed max flow in g3: " + maxFlow);
        Assertions.assertEquals(EXPECTED_FLOW_OF_G3, maxFlow);
    }
}
