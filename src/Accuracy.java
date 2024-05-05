import bagel.*;

/**
 * Class for dealing with accuracy of pressing the notes
 */
public class Accuracy {
    public static final int PERFECT_SCORE = 10;
    public static final int GOOD_SCORE = 5;
    public static final int BAD_SCORE = -1;
    public static final int MISS_SCORE = -5;
    public static final int NOT_SCORED = 0;
    public static final String PERFECT = "PERFECT";
    public static final String GOOD = "GOOD";
    public static final String BAD = "BAD";
    public static final String MISS = "MISS";
    public static final String DOUBLE_SCORE = "Double Score";
    public static final String SPEED_UP = "Speed Up";
    public static final String SLOW_DOWN = "Slow Down";
    public static final String CLEAR = "Clear";
    private static final int PERFECT_RADIUS = 15;
    private static final int GOOD_RADIUS = 50;
    private static final int BAD_RADIUS = 100;
    private static final int MISS_RADIUS = 200;
    private static final Font ACCURACY_FONT = new Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private String currAccuracy = null;
    private String currEffect = null;
    private int frameCount = 0;

    public void setAccuracy(String accuracy, String effect) {
        currAccuracy = accuracy;
        currEffect = effect;
        frameCount = 0;
    }

    public int evaluateScore(int height, int targetHeight, boolean triggered, String effect) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= PERFECT_RADIUS) {
                setAccuracy(PERFECT, effect);
                return PERFECT_SCORE;
            } else if (distance <= GOOD_RADIUS) {
                setAccuracy(GOOD, effect);
                return GOOD_SCORE;
            } else if (distance <= BAD_RADIUS) {
                setAccuracy(BAD, effect);
                return BAD_SCORE;
            } else if (distance <= MISS_RADIUS) {
                setAccuracy(MISS, effect);
                return MISS_SCORE;
            }

        } else if (height >= (Window.getHeight())) {
            setAccuracy(MISS, null);
            return MISS_SCORE;
        }

        return NOT_SCORED;

    }

    public void update() {
        frameCount++;
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {

            if (currEffect != null) {
                switch(currEffect) {
                    case "Bomb":
                        ACCURACY_FONT.drawString(CLEAR, 
                        Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                        Window.getHeight()/2);
                        break;
                    case "DoubleScore":
                        ACCURACY_FONT.drawString(DOUBLE_SCORE, 
                        Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                        Window.getHeight()/2);
                        break;
                    case "SpeedUp":
                        ACCURACY_FONT.drawString(SPEED_UP, 
                        Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                        Window.getHeight()/2);
                        break;
                    case "SlowDown":
                        ACCURACY_FONT.drawString(SLOW_DOWN, 
                        Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                        Window.getHeight()/2);
                        break;
                }
            }
            else {
                ACCURACY_FONT.drawString(currAccuracy,
                    Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                    Window.getHeight()/2);
            }
        

            
        }
    }
}
