import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Solution for SWEN20003 Project 1, Semester 2, 2023
 *
 * @author Clinton Lim
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final static String CSV_FILE_1 = "res/test1.csv";
    private final static String CSV_FILE_2 = "res/test2.csv";
    private final static String CSV_FILE_3 = "res/level3.csv";
    public final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int LEVEL_X_OFFSET = 100;
    private final static int LEVEL_Y_OFFSET = 190;
    private final static int NUMLEV_X_OFFSET = 160;
    private final static int NUMLEV_Y_OFFSET = 270;
    private final static int SCORE_LOCATION = 35;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private static final String LEVEL_SELECT = "Select Levels With\nNumber Keys";
    private static final String NUMBER_LEVELS = "1      2       3";
    private static final int CLEAR_SCORE = 150;
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String NEXT_LEVEL = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private final Accuracy accuracy = new Accuracy();
    private Lane[] lanes = new Lane[4];
    private int numLanes = 0;
    private int score = 0;
    private static int currFrame = 0;
    private Track track = new Track("res/track1.wav");
    private boolean levelOneStarted = false;
    private boolean levelTwoStarted = false;
    private boolean levelThreeStarted = false;
    private boolean finished = false;
    private boolean paused = false;
    private Enemy[] enemies = new Enemy[20];
    private int numEnemies = 0;
    private int currEnemies = 0;
    private Projectile[] projectiles = new Projectile[200];
    public int numProjectiles = 0;
    public int currProjectiles = 0;


    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }



    private void readCsv(String CSV_FILE) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    Lane lane = new Lane(laneType, pos);
                    lanes[numLanes++] = lane;
                } else {
                    // reading notes
                    String dir = splitText[0];
                    Lane lane = null;
                    for (int i = 0; i < numLanes; i++) {
                        if (lanes[i].getType().equals(dir)) {
                            lane = lanes[i];
                        }
                    }

                    if (lane != null) {
                        switch (splitText[1]) {
                            case "Normal":
                                Note note = new Note(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(note);
                                break;
                            case "Hold":
                                HoldNote holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addHoldNote(holdNote);
                                break;
                            case "Bomb":
                                BombNote bombNote = new BombNote(dir, Integer.parseInt(splitText[2]));
                                lane.addBombNote(bombNote);
                                break;
                            default:
                                SpecialNote specialNote = new SpecialNote(dir, splitText[1], Integer.parseInt(splitText[2]));
                                lane.addSpecialNote(specialNote);
                                break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!levelOneStarted && !levelTwoStarted && !levelThreeStarted) {
            // starting screen
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(LEVEL_SELECT,
                    TITLE_X + LEVEL_X_OFFSET, TITLE_Y + LEVEL_Y_OFFSET);
            INSTRUCTION_FONT.drawString(NUMBER_LEVELS, 
                    TITLE_X + NUMLEV_X_OFFSET, TITLE_Y + NUMLEV_Y_OFFSET);

            if (input.wasPressed(Keys.NUM_1)) {
                levelOneStarted = true;
                readCsv(CSV_FILE_1);
                // track.start();
            }

            if (input.wasPressed(Keys.NUM_2)) {
                levelTwoStarted = true;
                readCsv(CSV_FILE_2);
                // track.start();
            }

            if (input.wasPressed(Keys.NUM_3)) {
                levelThreeStarted = true;
                readCsv(CSV_FILE_3);
                // track.start();
            }
        } else if (finished) {
            // end screen
            if (score >= CLEAR_SCORE) {
                TITLE_FONT.drawString(CLEAR_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(CLEAR_MESSAGE)/2,
                        WINDOW_HEIGHT/2 - 100);
                INSTRUCTION_FONT.drawString(NEXT_LEVEL, 
                        WINDOW_WIDTH/2 - INSTRUCTION_FONT.getWidth(NEXT_LEVEL)/2, 
                        WINDOW_HEIGHT/2 + LEVEL_Y_OFFSET - 100);
                
                if (input.wasPressed(Keys.SPACE)) {
                    score = 0;
                    levelOneStarted = false;
                    levelTwoStarted = false;
                    levelThreeStarted = false;
                    lanes = new Lane[4];
                    numLanes = 0;
                    currFrame = 0;
                    enemies = new Enemy[20];
                    numEnemies = 0;
                    projectiles = new Projectile[200];
                    numProjectiles = 0;
                    finished = false;
                }
            } else {
                TITLE_FONT.drawString(TRY_AGAIN_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2,
                        WINDOW_HEIGHT/2 - 100);
                INSTRUCTION_FONT.drawString(NEXT_LEVEL, 
                        WINDOW_WIDTH/2 - INSTRUCTION_FONT.getWidth(NEXT_LEVEL)/2, 
                        WINDOW_HEIGHT/2 + LEVEL_Y_OFFSET - 100);
                if (input.wasPressed(Keys.SPACE)) {
                    score = 0;
                    levelOneStarted = false;
                    levelTwoStarted = false;
                    levelThreeStarted = false;
                    lanes = new Lane[4];
                    numLanes = 0;
                    currFrame = 0;
                    enemies = new Enemy[20];
                    numEnemies = 0;
                    projectiles = new Projectile[200];
                    numProjectiles = 0;
                    finished = false;
                }
            }
        } else if (levelOneStarted) {
            // gameplay level one

            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                    // track.run();
                }

                for (int i = 0; i < numLanes; i++) {
                    lanes[i].draw();
                }

            } else {
                currFrame++;
                for (int i = 0; i < numLanes; i++) {
                    score += lanes[i].update(input, accuracy);
                }

                accuracy.update();
                finished = checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                    // track.pause();
                }
            }
        } else if (levelTwoStarted) {
            // gameplay level two
            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                    // track.run();
                }

                for (int i = 0; i < numLanes; i++) {
                    lanes[i].draw();
                }

            } else {
                currFrame++;
                for (int i = 0; i < numLanes; i++) {
                    score += lanes[i].update(input, accuracy);
                }

                accuracy.update();
                finished = checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                    // track.pause();
                }
            }
        } else if (levelThreeStarted) {
            // gameplay level three
            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);
            Guardian guardian = new Guardian();

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                    // track.run();
                }

                for (int i = 0; i < numLanes; i++) {
                    lanes[i].draw();
                }

            } else {
                guardian.draw();
                currFrame++;
                int closestEnemy = -1;
                int maxDistance = 0;
                for (int i = currEnemies; i < numEnemies; i++) {
                    enemies[i].update();
                    int tmp = guardian.calculateEnemyDistance(enemies[i].getX(), enemies[i].getY());
                    
                    if (tmp >= maxDistance) {
                        maxDistance = tmp;
                        closestEnemy = i;
                    }

                    if (currEnemies < numEnemies) {

                        if (input.wasPressed(Keys.LEFT_SHIFT)) {
                            Projectile projectile = new Projectile(guardian.x, guardian.y, enemies[closestEnemy].getX(), enemies[closestEnemy].getY());
                            projectiles[numProjectiles++] = projectile;
                        }

                        for (int j = currProjectiles; j < numProjectiles; j++) {
                            projectiles[j].update();

                            int x_difference = Math.abs(enemies[i].getX() - (int)projectiles[j].vector.asPoint().x);
                            int y_difference = Math.abs(enemies[i].getY() - (int)projectiles[j].vector.asPoint().y);
                
                            if ((x_difference <= 62) && (y_difference <= 62)) {
                                enemies[i].deactivate();
                                projectiles[j].deactivate();
                            }

                            if (currProjectiles < numProjectiles) {
                                if (projectiles[currProjectiles].isCompleted()) {
                                    currProjectiles++;
                                }
                            }
                        }

                        if (enemies[currEnemies].isCompleted()) {
                            currEnemies++;
                        }
                        
                    }
                    
                }

                for (int i = 0; i < numLanes; i++) {
                    score += lanes[i].update(input, accuracy);
                    lanes[i].checkEnemyCollision(enemies, numEnemies, currEnemies);
                }

                if ((currFrame % Enemy.spawnRate) == 0) {
                    Enemy enemy = new Enemy();
                    enemies[numEnemies++] = enemy;
                    enemies[currEnemies].active = true;
                }

                accuracy.update();
                finished = checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                    // track.pause();
                }
            }
        }

    }

    public static int getCurrFrame() {
        return currFrame;
    }

    private boolean checkFinished() {
        for (int i = 0; i < numLanes; i++) {
            if (!lanes[i].isFinished()) {
                return false;
            }
        }
        return true;
    }
}
