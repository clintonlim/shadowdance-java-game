import bagel.*;
import bagel.util.*;

public class Projectile {
    private final Image image = new Image("res/arrow.PNG");
    public static int speed = 6;
    public int numProjectiles = 0;
    private double angle;
    private boolean active = false;
    private boolean completed = false;
    public Vector2 vector;
    public Vector2 targetVector;

    public Projectile(int guardianX, int guardianY, int enemyX, int enemyY) {
        // this.x = guardianX;
        // this.y = guardianY;
        angle = Math.atan(Math.abs(guardianY - enemyY) / Math.abs(guardianX - enemyX));

        this.vector = new Vector2(guardianX, guardianY);
        this.targetVector = new Vector2(enemyX, enemyY);
    }

    public boolean isActive() {
        return active;
    }
    public boolean isCompleted() {return completed;}

    public void deactivate() {
        active = false;
        completed = true;
    }

    /**
     * draws the projectile given an angle
     */
    public void draw() {
        DrawOptions drawOptions = new DrawOptions();
        image.draw(vector.asPoint().x, vector.asPoint().y, drawOptions.setRotation(angle + Math.toRadians(180)));
    }

    /**
     * updates all projectiles in the game
     */
    public void update() {
        draw();
       
        Vector2 dif = vector.sub(targetVector);
        dif = dif.normalised();
        dif = dif.mul(speed);
        vector = vector.sub(dif);

        if (dif.asPoint().x < 0) {
            deactivate();
        }
    }
}
