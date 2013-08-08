package com.github.axet.lookup.common;

public class LessCompare {

    static public int compareBigFirst(double o1, double o2, double val) {
        if (Math.abs(o1 - o2) < val)
            return 0;

        return compareBigFirst(o1, o2);
    }

    // desc algorithm (high comes at first [0])
    static public int compareBigFirst(int o1, int o2) {
        return new Integer(o2).compareTo(new Integer(o1));
    }

    // desc algorithm (high comes at first [0])
    static public int compareBigFirst(float o1, float o2) {
        return new Float(o2).compareTo(new Float(o1));
    }

    static public int compareBigFirst(double o1, double o2) {
        return new Double(o2).compareTo(new Double(o1));
    }

    // asc algorithm (low comes at first [0])
    static public int compareSmallFirst(int o1, int o2) {
        return new Integer(o1).compareTo(new Integer(o2));
    }

    static public int compareBigFirst(int o1, int o2, int val) {
        if (Math.abs(o1 - o2) < val)
            return 0;

        return compareBigFirst(o1, o2);
    }

    static public int compareSmallFirst(double o1, double o2) {
        return new Double(o1).compareTo(new Double(o2));
    }

}
