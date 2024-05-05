import bagel.Image;

import bagel.*;
import java.util.Random;

/**
 * Class for enemies
 */
public class Enemy {
    private final Image image = new Image("res/enemy.PNG");
    public final static int spawnRate = 600;
    public final static int xRange = 800;
    public final static int yRange = 500;
    public final static int rangeOffset = 100;
    public static int speed = 1;
    private int x;
    private int y;
    private boolean goingLeft;
    public boolean active = false;
    private boolean completed = false;
    Random rand = new Random();

    public Enemy() {
        x = rand.nextInt(xRange) + rangeOffset;
        y = rand.nextInt(yRange) + rangeOffset;

        int z = rand.nextInt(1);
        if (z == 0) {
            goingLeft = true;
        } else {
            goingLeft = false;
        }
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void deactivate() {
        active = false;
        completed = true;
    }

    public boolean isActive() {
        return active;
    }
    public boolean isCompleted() {return completed;}

    /**
     * draws the enemy
     */
    public void draw() {
        image.draw(x, y);
    }

    /**
     * updates all the enemies in the game
     */
    public void update() {
        draw();

        if (goingLeft == true) {
            x -= speed;
            if (x <= 100) {
                goingLeft = false;
            }

        } else if (goingLeft == false) {
            x += speed;
            if (x >= 900) {
                goingLeft = true;
            }

        }
    }
}
