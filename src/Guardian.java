import bagel.*;

/**
 * Class for guardian
 */
public class Guardian {
    private final Image image = new Image("res/guardian.PNG");
    public int x = 800;
    public int y = 600;

    // public Guardian() {

    // }

    /**
     * draws the guardian
     */
    public void draw() {
        image.draw(x, y);
    }

    /**
     * 
     * @param enemyX x-coordinate of enemy
     * @param enemyY y-coordinate of enemy
     * @return manhattan distance between guardian(x,y) and enemy(x,y)
     */
    public int calculateEnemyDistance(int enemyX, int enemyY) {
        return Math.abs(x - enemyX) + Math.abs(y - enemyY);
    }

}
