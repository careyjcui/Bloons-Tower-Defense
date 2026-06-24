package io.github.some_example_name;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Initializes all game elements, takes user input, and runs the game.
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 * 
 * @author Carey Cui
 * @author Charlie Qiu
 * @author Albert Hwang
 * @version 2025/06/06
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Map1 map1;
    private int lives = 150;
    private int money = 200;
    private int score = 0;
    private int highScore = 0;
    private Array<Tower> towers = new Array<>();
    private Array<Bloon> bloons = new Array<>();
    private ArrayList<TowerUI> towerUIs = new ArrayList<>();
    private boolean creatingTower = false;
    private int towerUIElement = 0;
    private int round = 0;
    private ArrayList<Bloon>[] rounds;
    private Bloon nextSpawn;
    private int frameCount = 0;
    private Texture moneyImage;
    private Texture livesImage;
    private BitmapFont font;
    private BitmapFont roundFont; 
    private Sound building;
    private Music bgm;
    private Texture playButton;
    private ShapeRenderer shapeRenderer;
    private boolean collidingWithTrack = false;
    private boolean collidingWithTower = false;
    private String screen = "start";
    private Texture startScreen;
    private Texture winscreen;
    private Texture loseScreen;
    private Texture HTP;
    private boolean upgrading = false;
    private int currentTowerIndex;
    private Texture upgradeBackground;
    private Texture upgradeButton;

    /**
     * Initializes all game assets and state variables.
     * This method is automatically called when the application is launched.
     * It loads textures, sounds, fonts, and game screens, sets up the initial tower UI elements,
     * prepares the round data, and starts background music. It also reads the saved high score from a local file.
     */
    @Override
    public void create() {
        //read highscore from save file
        try {
            BufferedReader br = new BufferedReader(new FileReader("../core/Savefile.txt"));
            highScore = Integer.parseInt(br.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        map1 = new Map1();
        batch = new SpriteBatch();
        playButton = new Texture("TowerList/playButton.png");

        upgradeBackground = new Texture("TowerList/background.png");
        upgradeButton = new Texture("upgrade.png");

        startScreen = new Texture("Screens/startScreen.png");
        winscreen = new Texture("Screens/victory.png");
        loseScreen = new Texture("Screens/gameover.png");
        HTP = new Texture("Screens/howToPlay.png");

        towerUIs.add(new TowerUI(0, 1100, 600, 170));
        towerUIs.add(new TowerUI(1, 1180, 600, 320));
        towerUIs.add(new TowerUI(2, 1100, 475, 400));

        shapeRenderer = new ShapeRenderer();

        //initialize audio
        Bloon.popSound = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/bloonPop.mp3"));
        building = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/building.mp3"));
        Tower.explosion = Gdx.audio.newSound(Gdx.files.internal("SoundEffects/explosion.mp3"));

        //Background music intializing and looping
        bgm = Gdx.audio.newMusic(Gdx.files.internal("SoundEffects/bgm.mp3"));
        bgm.setLooping(true);
        bgm.setVolume(0.3f);
        bgm.play();

        //initialize lives & money image
        moneyImage = new Texture("Others/money.png");
        livesImage = new Texture("Others/lives.png");

        //initialize font
        font = new BitmapFont();
        roundFont = new BitmapFont();
        roundFont.getData().setScale(2f);

        //load Bloons
        rounds = map1.getRounds();
    }
    /**
     * Renders the current frame of the game.
     * This method is called continuously and handles all screen drawing and user interactions.
     * It updates game logic based on the current screen state: start menu, how-to-play screen,
     * active gameplay, win screen, or loss screen.
     *
     * During gameplay, it handles:
     * User input for placing and upgrading towers
     * Bloon spawning, movement, and collision detection
     * Round progression and end conditions
     * Rendering of game objects, UI, and range indicators
     */
    @Override
    public void render() {
        //clear the screen
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        if (screen.equals("start")) {
            //draw start screen
            batch.draw(startScreen, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            //check if the user clicked the play or exit button
            if (Gdx.input.justTouched()) {
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                float worldY = Gdx.graphics.getHeight() - y;
                //check if the user clicked the play button
                if (new Rectangle(260, 80, 250, 225).contains(x, worldY)) {
                    //go to game screen
                    screen = "game";
                }
                //check if the user clicked the exit button
                else if (new Rectangle(690, 80, 250, 225).contains(x, worldY)) {
                    screen = "HTP";
                }
            }
            batch.end();
        }
        else if (screen.equals("HTP")) {
            //draw how to play screen
            batch.draw(HTP, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if (Gdx.input.justTouched()) {
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                float worldY = Gdx.graphics.getHeight() - y;
                //check if the user clicked the back button
                if (new Rectangle(400, 40, 450, 100).contains(x, worldY)) {
                    screen = "start";
                }
            }
            batch.end();
        }
        else if (screen.equals("game")) {
            //update gamestate if the user lost
            if (lives <= 0) {
                screen = "endloss";
            }
            map1.render(batch);
            //take user input
            int xPos = Gdx.input.getX();
            int yPos = Gdx.input.getY();
            float worldY = Gdx.graphics.getHeight() - yPos;
            if (Gdx.input.justTouched()) {
                //Check if the user is upgrading a tower
                for (int i = 0; i < towers.size; i++) {
                    Tower tower = towers.get(i);
                    //check if the user clicked on a tower to upgrade
                    if (Math.sqrt(Math.pow(tower.getX() - xPos, 2) + Math.pow(tower.getY() - worldY, 2)) < tower.getRadius()) {
                        upgrading = true;
                        currentTowerIndex = i;
                        break;
                    }
                    else{
                        if (!new Rectangle(0,0,400,100).contains(xPos, worldY)) {
                            upgrading = false;
                        }
                    }
                }
                //check if the user clicked on a tower UI element to create a tower
                for (int i = 0; i < towerUIs.size(); i++) {
                    TowerUI towerUI = towerUIs.get(i);
                    if (towerUI.getBounds().contains(xPos, worldY)) {
                        creatingTower = true;
                        towerUIElement = i;
                        break;
                    }
                }
            }
            //check if the user is upgrading
            if (upgrading) {
                //draw upgrade UI
                boolean canUpgrade = towers.get(currentTowerIndex).getUpgradeLevel() < 3;
                batch.draw(upgradeBackground, -15, 0, 400, 100);
                //check if the tower can be upgraded
                if (canUpgrade){
                    batch.draw(upgradeButton, 0, 0, 100, 100);
                    Tower tower = towers.get(currentTowerIndex);
                    font.draw(batch, "Upgrade Level: " + tower.getUpgradeLevel(), 100, 80);
                    font.draw(batch, "Next Upgrade Cost: " + 300, 100, 60);
                    //check if the user has clicked
                    if (Gdx.input.justTouched()) {
                        int x = Gdx.input.getX();
                        int y = Gdx.input.getY();
                        float worldY2 = Gdx.graphics.getHeight() - y;
                        //check if the user clicked the upgrade button
                        if (new Rectangle(0, 0, 100, 100).contains(x, worldY2)) {
                            //upgrade the tower if the user has enough money
                            if (300 <= money) {
                                tower.upgrade();
                                money -= 300;
                                building.play();
                            }
                        }
                    }
                }
                else{
                    //if the tower cannot be upgraded, show a message
                    font.draw(batch, "Max Upgrade Level Reached", 100, 80);
                }
                
            }
            //check if the user is creating a tower
            if (creatingTower){
                //break out of the loop if the user clicks on a tower UI element
                breakPoint:
                if (Gdx.input.justTouched()) {
                    //check if the user clicked on a tower UI element
                    for (TowerUI towerUI : towerUIs) {
                        if (towerUI.getBounds().contains(xPos, worldY)) {
                            towerUIElement = towerUIs.indexOf(towerUI);
                            break breakPoint;
                        }
                    }
                    //check if the placement of the tower is valid
                    if (towerUIElement == 0 && money >= 170) {
                        DartMonkey dartMonkey = new DartMonkey(xPos, worldY);
                        collidingWithTower = false;
                        //check if the tower collides with other towers
                        for (Tower tower: towers){
                            if (Math.sqrt(Math.pow(tower.getX() - xPos, 2) + Math.pow(tower.getY() - worldY, 2)) < tower.getRadius() + dartMonkey.getRadius()) {
                                collidingWithTower = true;
                                break;
                            }
                        }
                        if (dartMonkey.collideTrack(map1) || collidingWithTower) {
                            collidingWithTrack = true;
                        } else {
                            //place tower if it does not collide with track or other towers
                            collidingWithTrack = false;
                            towers.add(dartMonkey);
                            money -= 170;
                            building.play();
                        }
                        //check if the placement of the tower is valid
                    } else if (towerUIElement == 1 && money >= 320) {
                        Cannon cannon = new Cannon(xPos, worldY);
                        collidingWithTower = false;
                        //check if the tower collides with other towers
                        for (Tower tower: towers){
                            if (Math.sqrt(Math.pow(tower.getX() - xPos, 2) + Math.pow(tower.getY() - worldY, 2)) < tower.getRadius() + cannon.getRadius()) {
                                collidingWithTower = true;
                                break;
                            }
                        }
                        if (cannon.collideTrack(map1) || collidingWithTower) {
                            collidingWithTrack = true;
                        } else {
                            //place tower if it does not collide with track or other towers
                            collidingWithTrack = false;
                            towers.add(cannon);
                            money -= 320;
                            building.play();

                        }
                        //check if the placement of the tower is valid
                    } else if (towerUIElement == 2 && money >= 400) {
                        TackShooter tackShooter = new TackShooter(xPos, worldY);
                        collidingWithTower = false;
                        //check if the tower collides with other towers
                        for (Tower tower: towers){
                            if (Math.sqrt(Math.pow(tower.getX() - xPos, 2) + Math.pow(tower.getY() - worldY, 2)) < tower.getRadius() + tackShooter.getRadius()) {
                                collidingWithTower = true;
                                break;
                            }
                        }
                        if (tackShooter.collideTrack(map1) || collidingWithTower) {
                            collidingWithTrack = true;
                        } else {
                            //place tower if it does not collide with track or other towers
                            collidingWithTrack = false;
                            towers.add(tackShooter);
                            money -= 400;
                            building.play();
                        }
                    }
                    creatingTower = false;
                }
                else{
                    //if the user is creating a tower, draw the tower UI element so the user can see where they are placing the tower
                    if (towerUIElement == 0) {
                        DartMonkey dartMonkey = new DartMonkey(xPos, worldY);
                        //check if the tower collides with other towers
                        for (Tower tower: towers) {
                            if (Math.sqrt(Math.pow(tower.getX() - xPos, 2) + Math.pow(tower.getY() - worldY, 2)) < tower.getRadius() + dartMonkey.getRadius()) {
                                collidingWithTower = true;
                                break;
                            } else {
                                collidingWithTower = false;
                            }
                        }
                        //check if the user has enough money to create the tower
                        if (money < 170) {
                            creatingTower = false;
                        }
                        else{
                            collidingWithTrack = dartMonkey.collideTrack(map1);
                            dartMonkey.draw(batch);
                        }
                    } else if (towerUIElement == 1) {
                        Cannon cannon = new Cannon(xPos, worldY);
                        //check if the tower collides with other towers
                        for (Tower tower: towers) {
                            if (Math.sqrt(Math.pow(tower.getX() - xPos, 2) + Math.pow(tower.getY() - worldY, 2)) < tower.getRadius() + cannon.getRadius()) {
                                collidingWithTower = true;
                                break;
                            } else {
                                collidingWithTower = false;
                            }
                        }
                        //check if the user has enough money to create the tower
                        if (money < 320) {
                            creatingTower = false;
                        }
                        else{
                            collidingWithTrack = cannon.collideTrack(map1);
                            cannon.draw(batch);
                        }
                    } else if (towerUIElement == 2) {
                        TackShooter tackShooter = new TackShooter(xPos, worldY);
                        //check if the tower collides with other towers
                        for (Tower tower: towers) {
                            if (Math.sqrt(Math.pow(tower.getX() - xPos, 2) + Math.pow(tower.getY() - worldY, 2)) < tower.getRadius()+ tackShooter.getRadius()) {
                                collidingWithTower = true;
                                break;
                            } else {
                                collidingWithTower = false;
                            }
                        }
                        //check if the user has enough money to create the tower
                        if (money < 400) {
                            creatingTower = false;
                        }
                        else{
                            collidingWithTrack = tackShooter.collideTrack(map1);
                            tackShooter.draw(batch);
                        }
                    }
                }
            }

            //bloon logic
            //add next bloon to list of live bloons
            if (nextSpawn != null) {
                bloons.add(nextSpawn);
            }
            //prepare next bloon to spawn
            if (!rounds[round].isEmpty() && frameCount % (30 - 5 * (round / 10)) == 0) {
                nextSpawn = rounds[round].get(0);
                rounds[round].remove(0);
            } else {
                nextSpawn = null;
            }
            //check for clicking start next round button
            if (bloons.size == 0 && rounds[round].isEmpty()){
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                float wY = Gdx.graphics.getHeight() - y;
                if (round == rounds.length-1) {
                    //if last round, show end screen
                    screen = "endwin";
                    round = rounds.length; // prevent going past last round
                    screen = "endwin";
                    //calculate score and update highscore
                    score = lives * money;
                    highScore = Math.max(score, highScore);
                    //write highscore to save file
                    try {
                        PrintWriter pw = new PrintWriter("../core/Savefile.txt");
                        pw.print(highScore);
                        pw.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (Gdx.input.justTouched() && new Rectangle(1190, 10, 80, 80).contains(x, wY)) {
                    //go to next round
                    frameCount = 0;
                    round++;
                }
            }
            //move and draw bloons
            for (Bloon bloon: bloons) {
                if (bloon.getHp() <= -1) {
                    bloon.dispose();
                    bloons.removeValue(bloon, false);
                    continue;
                }
                bloon.updatePos();
                //check if the bloon has bypassed the defense
                if (bloon.endPoint(map1)) {
                    lives -= bloon.getHp() + 1;
                    bloons.removeValue(bloon, false);
                } else {
                    bloon.draw(batch, map1);
                }
            }

            //handle tower logic and increase money gained from bloon kills
            for (Tower tower: towers) {
                money += tower.render(frameCount, bloons, map1);
                tower.draw(batch);
            }

            //increment frame counter
            frameCount++;

            //draw tower UI background
            batch.draw(playButton, 1190, 10, 80, 80);
            
            //draw tower UIs
            for (TowerUI towerUI : towerUIs) {
                towerUI.draw(batch);
            }

            //draw lives image & text
            batch.draw(livesImage,10,667,50,50);
            font.draw(batch, "" + lives, 20, 664);  
            
            //draw money image & text
            batch.draw(moneyImage, 100,667,50,50);
            font.draw(batch, "" + money, 110, 664);
            roundFont.draw(batch, "Round: " + round + "/40", 800,700);
            batch.end();

            //draw range circle if the user is creating a tower or upgrading
            if ((creatingTower && !Gdx.input.justTouched())|| upgrading) {
                //setting the range
                float range = 0;
                if (upgrading){
                    range = towers.get(currentTowerIndex).getRange();
                }
                else{
                    if (towerUIElement == 0) {
                        range = new DartMonkey(xPos, worldY).getRange();
                    } else if (towerUIElement == 1) {
                        range = new Cannon(xPos, worldY).getRange();
                    } else if (towerUIElement == 2) {
                        range = new TackShooter(xPos, worldY).getRange();
                    }
                }
                //setting the style and color
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                if (collidingWithTrack || collidingWithTower) {
                    shapeRenderer.setColor(1, 0, 0, 0.3f);
                } else {
                    shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 0.3f);
                }
                //draw the range circle depending on whether the user is upgrading or creating a tower
                if (upgrading){
                    shapeRenderer.circle(towers.get(currentTowerIndex).getX(), towers.get(currentTowerIndex).getY(), range);
                }
                else{
                    shapeRenderer.circle(xPos, worldY, range);
                }
                shapeRenderer.end();

                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
        }
        else if (screen.equals("endloss")) {
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            batch.draw(loseScreen, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            //Buttons
            if (Gdx.input.justTouched()) {
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                float worldY = Gdx.graphics.getHeight() - y;
                //check if the user clicked the restart button
                if (new Rectangle(300,60, 685, 120).contains(x, worldY)) {
                    //quit game
                    Gdx.app.exit();
                }
                else if (new Rectangle(300, 200, 685, 120).contains(x, worldY)) {
                    screen = "game";
                    resetGame();
                }
            }
            //draw lives image & text
            batch.draw(livesImage,10,667,50,50);
            font.draw(batch, "" + lives, 20, 664);  
            
            //draw money image & text
            batch.draw(moneyImage, 100,667,50,50);
            font.draw(batch, "" + money, 110, 664);

            font.draw(batch, "High Score: " + highScore, 1100, 700);
            batch.end();
        }
        else if (screen.equals("endwin")) {
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            batch.draw(winscreen, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            //Buttons
            if (Gdx.input.justTouched()) {
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                float worldY = Gdx.graphics.getHeight() - y;
                //check if the user clicked the restart button
                if (new Rectangle(300, 40, 685, 120).contains(x, worldY)) {
                    screen = "start";
                    resetGame();
                }
                else if (new Rectangle(300, 180, 685, 120).contains(x, worldY)) {
                    screen = "game";
                    resetGame();
                }
            }

            //draw lives image & text
            batch.draw(livesImage,10,667,50,50);
            font.draw(batch, "" + lives, 20, 664);
            
            //draw money image & text
            batch.draw(moneyImage, 100,667,50,50);
            font.draw(batch, "" + money, 110, 664);

            font.draw(batch, "High Score: " + highScore, 1100, 700);
            font.draw(batch, "Score: " + score, 1100, 650);
            batch.end();
        }
    }
    /**
     * Resets the game to its initial state.
     * This method is called when the player chooses to restart the game after a win or loss.
     * It clears all active bloons and towers, resets player stats (lives, money, score, round),
     * and disables any tower placement or upgrade states.
     */
    public void resetGame(){
        //reset all game variables to their initial state
        money = 200;
        lives = 150;
        score = 0;
        round = 0;
        frameCount = 0;
        bloons.clear();
        towers.clear();
        nextSpawn = null;
        creatingTower = false;
        collidingWithTrack = false;
        collidingWithTower = false;
    }
    /**
     * Releases all allocated resources to prevent memory leaks.
     * This method is called automatically when the application is closed.
     * It disposes of all graphical assets (textures, fonts), audio resources,
     * shape renderers, and any objects with disposable components.
     */
    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        map1.dispose();
        font.dispose();
        roundFont.dispose();
        bgm.dispose();
        for (TowerUI towerUI : towerUIs) {
            towerUI.dispose();
        }
    }
}
