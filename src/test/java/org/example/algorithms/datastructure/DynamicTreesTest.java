package org.example.algorithms.datastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DynamicTreesTest {

    private DynamicTrees dynTrees;

    @BeforeEach
    public void setUp() {
        dynTrees = new DynamicTrees(9);
        dynTrees.link(1, 0);
        dynTrees.link(2, 0);
        dynTrees.link(3, 0);
        dynTrees.link(4, 2);
        dynTrees.link(5, 2);
        dynTrees.link(7, 6);
        dynTrees.link(8, 6);
    }

    @Test
    public void testFindRoot() {
        DynamicTrees dt = new DynamicTrees(dynTrees);
        System.out.println("dt: " + dt);

        assertEquals(0, dt.findRoot(0));
        assertEquals(0, dt.findRoot(1));
        assertEquals(0, dt.findRoot(2));
        assertEquals(0, dt.findRoot(3));
        assertEquals(0, dt.findRoot(4));
        assertEquals(0, dt.findRoot(5));

        assertEquals(6, dt.findRoot(6));
        assertEquals(6, dt.findRoot(7));
        assertEquals(6, dt.findRoot(8));
    }

    @Test
    public void testCut() {
        DynamicTrees dt = new DynamicTrees(dynTrees);
        dt.cut(2);
        System.out.println("dt after cut: " + dt);

        assertEquals(0, dt.findRoot(0));
        assertEquals(0, dt.findRoot(1));
        assertEquals(0, dt.findRoot(3));

        assertEquals(2, dt.findRoot(2));
        assertEquals(2, dt.findRoot(4));
        assertEquals(2, dt.findRoot(5));

        assertEquals(6, dt.findRoot(6));
        assertEquals(6, dt.findRoot(7));
        assertEquals(6, dt.findRoot(8));

        dt.link(6, 4);
        System.out.println("dt after linking 6 to 4: " + dt);

        assertEquals(0, dt.findRoot(0));
        assertEquals(0, dt.findRoot(1));
        assertEquals(0, dt.findRoot(3));

        assertEquals(2, dt.findRoot(2));
        assertEquals(2, dt.findRoot(4));
        assertEquals(2, dt.findRoot(5));

        assertEquals(2, dt.findRoot(6));
        assertEquals(2, dt.findRoot(7));
        assertEquals(2, dt.findRoot(8));
    }

    @Test
    public void testAddCost() {
        DynamicTrees dt = new DynamicTrees(dynTrees);
        dt.addCost(2, 3);
        dt.addCost(8, 3);
        dt.addCost(1, 1);
        dt.addCost(5, 2);
        dt.addCost(7, 4);

        System.out.println("dt after add cost: " + dt);

        assertEquals(6, dt.findCost(0));
        assertEquals(6, dt.findCost(1));
        assertEquals(6, dt.findCost(2));
        assertEquals(6, dt.findCost(4));
        assertEquals(6, dt.findCost(5));
        assertEquals(6, dt.findCost(3));

        assertEquals(7, dt.findCost(6));
        assertEquals(7, dt.findCost(7));
        assertEquals(7, dt.findCost(8));
    }
}
