public class Message {
    private final static int INITIAL_FRAMES = 40;

    private String laneType;
    private String score;
    private int framesRemaining;

    public Message(String laneType, String score) {
        this.laneType = laneType;
        this.score = score;
        this.framesRemaining = INITIAL_FRAMES;
    }

    public String getLaneType() {
        return laneType;
    }

    public String getScore() {
        return score;
    }

    public int getFramesRemaining() {
        return framesRemaining;
    }

    public void setFramesRemaining(int framesRemaining) {
        this.framesRemaining = framesRemaining;
    }
}
