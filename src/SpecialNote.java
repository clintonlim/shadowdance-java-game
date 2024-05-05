import bagel.*;

/**
 * Class for normal notes
 */
public class SpecialNote {
    private final Image image;
    private final int appearanceFrame;
    private final String effect;
    public static int speed = 2;
    public static int scoreMultiplier = 1;
    public static int multiplierFrame;
    private int y = 100;
    private boolean active = false;
    private boolean completed = false;
    private int frameCount = 0;

    public SpecialNote(String dir, String effect, int appearanceFrame) {
        image = new Image("res/note" + effect + ".PNG");
        this.effect = effect;
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

    /**
     * updates all special notes in the game
     */
    public void update() {
        if (active) {
            frameCount++;
            y += speed;

            if (frameCount >= (multiplierFrame + 480)) {
                scoreMultiplier = 1;
            }

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
            int score = accuracy.evaluateScore(y, targetHeight, input.wasPressed(relevantKey), this.effect);

            if (score != Accuracy.NOT_SCORED) {

                if (score == Accuracy.MISS_SCORE) {
                    deactivate();
                    return score;
                } else if (score != Accuracy.NOT_SCORED) {
                    score = implementEffect(effect, score);
                    deactivate();
                    return score;
                }
                
            }

        }

        return 0;
    }

    /**
     * 
     * @param effect name of effect to be applied
     * @param score score to be returned from given effect
     * @return score to be given
     * 
     * applies a given effect to the game and returns points to be added to total score
     */
    public int implementEffect(String effect, int score) {
        
        switch(effect) {
            case "SpeedUp":
                speed += 1;
                Note.speed += 1;
                HoldNote.speed += 1;
                BombNote.speed += 1;
                return 15;
            case "SlowDown":
                speed -= 1;
                Note.speed -= 1;
                HoldNote.speed -= 1;
                BombNote.speed -= 1;
                return 15;
            case "DoubleScore":
                scoreMultiplier = 2;
                multiplierFrame = frameCount;
                return 0;
            default:
                return score;
        }
    }

}
