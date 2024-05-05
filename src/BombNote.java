import bagel.*;

/**
 * Class for normal notes
 */
public class BombNote {
    private final Image image;
    private final int appearanceFrame;
    public static int speed = 2;
    private int y = 100;
    private boolean active = false;
    private boolean completed = false;
    public boolean hit = false;

    public BombNote(String dir, int appearanceFrame) {
        image = new Image("res/noteBomb.png");
        this.appearanceFrame = appearanceFrame;
    }

    public boolean isActive() {
        return active;
    }
    public boolean isCompleted() {return completed;}

    public void deactivate() {
        active = false;
        completed = true;
    }

    public void update() {
        if (active) {
            y += speed;
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateScore(y, targetHeight, input.wasPressed(relevantKey), "Bomb");

            if (score != Accuracy.NOT_SCORED) {

                if (score == Accuracy.MISS_SCORE) {
                    deactivate();
                }
                else if (score != Accuracy.MISS_SCORE) {
                    // BOMB HIT
                    hit = true;
                    deactivate();
                }
                return 0;
            }

        }
        return 0;
    }

}
