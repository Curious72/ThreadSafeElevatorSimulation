package impl;

import interfaces.IController;

public class Floor implements Comparable<Floor>{
    private int level;
    private IController controller;

    public Floor(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    @Override
    public int compareTo(Floor o) {
        return this.level - o.level;
    }

    public String toString()
    {
        return String.valueOf(this.getLevel());
    }
}
