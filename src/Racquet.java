/**
 * Earl Timothy D. Malaki
 * BSCS - II | CMSC 22 - OOP
 * MP #4 - Pong
 */

import java.awt.*;

public class Racquet {

    private final int WIDTH = 10;
    private final int HEIGHT = 60;

    private int x;
    private int y = 120;
    private int ya = 0;

    // receives x coordinate
    // player 1 x = 10, player 2 x = 380; passed from Game.java
    public Racquet(int x) {
        this.x = x;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getYa() {
        return ya;
    }

    public void setYa(int ya) {
        this.ya = ya;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }
}