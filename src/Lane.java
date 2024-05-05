import bagel.*;

/**
 * Class for the lanes which notes fall down
 */
public class Lane {
    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    private final String type;
    private final Image image;
    private final Note[] notes = new Note[100];
    public int numNotes = 0;
    private final HoldNote[] holdNotes = new HoldNote[20];
    public int numHoldNotes = 0;
    private final BombNote[] bombNotes = new BombNote[20];
    public int numBombNotes = 0;
    private final SpecialNote[] specialNotes = new SpecialNote[20];
    public int numSpecialNotes = 0;
    private Keys relevantKey;
    private final int location;
    private int currNote = 0;
    private int currHoldNote = 0;
    private int currBombNote = 0;
    private int currSpecialNote = 0;

    public Lane(String dir, int location) {
        this.type = dir;
        this.location = location;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
            case "Special":
                relevantKey = Keys.SPACE;
                break;
        }
    }

    public String getType() {
        return type;
    }

    /**
     * 
     * @param enemies list of all enemies
     * @param numEnemies number of total enemies
     * @param currEnemies number of current enemies
     * 
     * checks collision between an enemy and any active note
     */
    public void checkEnemyCollision(Enemy[] enemies, int numEnemies, int currEnemies) {
        for (int i = currEnemies; i < numEnemies; i++) {
            for (int j = currNote; j < numNotes; j++) {
                int x_difference = Math.abs(enemies[i].getX() - location);
                int y_difference = Math.abs(enemies[i].getY() - notes[j].getY());
                
                if ((x_difference <= 104) && (y_difference <= 104) && (enemies[i].isActive()) == true) {
                    notes[j].deactivate();
                }
            }
        }
    }

    /**
     * updates all the notes in the lane
     */
    public int update(Input input, Accuracy accuracy) {
        draw();

        for (int i = currNote; i < numNotes; i++) {
            notes[i].update();
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes[j].update();
        }

        for (int l = currBombNote; l < numBombNotes; l++) {
            bombNotes[l].update();
        }

        for (int k = currSpecialNote; k < numSpecialNotes; k++) {
            specialNotes[k].update();
        }

        if (currNote < numNotes) {
            int score = notes[currNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (notes[currNote].isCompleted()) {
                currNote++;
                return score;
            }
            
        }

        if (currHoldNote < numHoldNotes) {
            int score = holdNotes[currHoldNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (holdNotes[currHoldNote].isCompleted()) {
                currHoldNote++;
                return score;
            }
            
        }

        if (currBombNote < numBombNotes) {
            int score = bombNotes[currBombNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            boolean isHit = bombNotes[currBombNote].hit;

            if (bombNotes[currBombNote].isCompleted()) {
                currBombNote++;

                if (isHit == true) {
                    if (notes[currNote] != null) {
                        notes[currNote].deactivate();
                    }
                    
                    if (holdNotes[currHoldNote] != null) {
                        holdNotes[currHoldNote].deactivate();
                    }
                    
                    if (specialNotes[currSpecialNote] != null) {
                        specialNotes[currSpecialNote].deactivate();
                    }
                }
                
                return score;
            }
            
        }

        if (currSpecialNote < numSpecialNotes) {
            int score = specialNotes[currSpecialNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (specialNotes[currSpecialNote].isCompleted()) {
                currSpecialNote++;
                return score;
            }
            
        }

        return Accuracy.NOT_SCORED;
    }

    public void addNote(Note n) {
        notes[numNotes++] = n;
    }

    public void addHoldNote(HoldNote hn) {
        holdNotes[numHoldNotes++] = hn;
    }

    public void addBombNote(BombNote bn) {
        bombNotes[numBombNotes++] = bn;
    }

    public void addSpecialNote(SpecialNote sn) {
        specialNotes[numSpecialNotes++] = sn;
    }

    /**
     * Finished when all the notes have been pressed or missed
     */
    public boolean isFinished() {
        for (int i = 0; i < numNotes; i++) {
            if (!notes[i].isCompleted()) {
                return false;
            }
        }

        for (int j = 0; j < numHoldNotes; j++) {
            if (!holdNotes[j].isCompleted()) {
                return false;
            }
        }

        for (int l = 0; l < numBombNotes; l++) {
            if (!bombNotes[l].isCompleted()) {
                return false;
            }
        }

        for (int k = 0; k < numSpecialNotes; k++) {
            if (!specialNotes[k].isCompleted()) {
                return false;
            }
        }

        return true;
    }

    /**
     * draws the lane and the notes
     */
    public void draw() {
        image.draw(location, HEIGHT);

        for (int i = currNote; i < numNotes; i++) {
            notes[i].draw(location);
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes[j].draw(location);
        }

        for (int l = currBombNote; l < numBombNotes; l++) {
            bombNotes[l].draw(location);
        }

        for (int k = currSpecialNote; k < numSpecialNotes; k++) {
            specialNotes[k].draw(location);
        }
    }

}
