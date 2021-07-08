package main;

public class Stone {
    final static int WHITE = 1;
    final static int BLACK = -1;
    final static int NONE = 0;

    private int state; // 1:white 0:none -1:black

    Stone() {
        this.state = Stone.NONE;
    }

    void put(int color) {
        this.state = color;
    }

    void reverse() {
        this.state *= -1;
    }

    int check() {
        return this.state;
    }
}
