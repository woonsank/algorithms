package org.example.algorithms.datastructure;

import java.util.Vector;

public class DynamicTrees {

    private int n; // number of vertex
    private Vector<Integer>[] cv; // child vertex indexes of each vertex
    private int[] p; // parent vertex index of each vertex
    private int[] c; // cost on each vertex

    public DynamicTrees(int n) {
        this.n = n;
        this.cv = new Vector[n];
        for (int i = 0; i < n; i++) {
            this.cv[i] = new Vector<>();
        }
        this.p = new int[n];
        for (int i = 0; i < n; i++) {
            this.p[i] = -1;
        }
        this.c = new int[n];
    }

    public DynamicTrees(DynamicTrees t) {
        this.n = t.n;
        this.cv = new Vector[t.cv.length];
        for (int i = 0; i < t.cv.length; i++) {
            this.cv[i] = new Vector<>(t.cv[i]);
        }
        this.p = new int[t.p.length];
        System.arraycopy(t.p, 0, this.p, 0, t.p.length);
        this.c = new int[t.c.length];
        System.arraycopy(t.c, 0, this.c, 0, t.c.length);
    }

    public int findRoot(int v) {
        int pv = -1;

        while (p[v] != -1) {
            pv = p[v];
            v = pv;
        }

        return pv != -1 ? pv : v;
    }

    public int findCost(int v) {
        return c[findRoot(v)];
    }

    public void addCost(int v, int x) {
        while (v != -1) {
            c[v] += x;
            v = p[v];
        }
    }

    public void link(int v, int w) {
        int expv = p[v];
        p[v] = w;
        cv[w].add(v);

        if (expv != -1) {
            cv[expv].remove(Integer.valueOf(v));
        }
    }

    public void cut(int v) {
        int expv = p[v];
        p[v] = -1;

        if (expv != -1) {
            cv[expv].remove(Integer.valueOf(v));
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DynamicTrees (n=" + n + "):\n");
        for (int i = 0; i < n; i++) {
            if (p[i] == -1) { // a root
                stringifyVertex(sb, i, 0);
            }
        }
        return sb.toString();
    }

    private void stringifyVertex(final StringBuilder sb, int v, int depth) {
        for (int j = 0; j < depth; j++)
            sb.append("  ");

        sb.append("[" + v + "] - " + c[v] + "\n");

        for (int i : cv[v]) {
            stringifyVertex(sb, i, depth + 1);
        }
    }
}
