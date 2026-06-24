package io.github.some_example_name;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
/**
 * Represents a UI element for selecting and displaying tower options in the tower selection panel.
 * Each UI element shows the tower's image, background, and cost.
 * 
 * @author Albert Hwang
 * @author Charlie Qiu
 * @version 2025/06/06
 */
public class TowerUI {
    /** The texture for the specific tower image */
    public Texture texture;
    public Texture background = new Texture("TowerList/background.png");
    private final float x,y;
    private static final BitmapFont font = new BitmapFont();
    private int towerType;
    private final int width = 80;
    private final int height = 100;
    private final int cost; 

    /**
     * Constructs a new tower UI element for a given tower type, position, and cost.
     * 
     * @param towerType the type of the tower
     * @param x the x-coordinate where the UI element will be drawn
     * @param y the y-coordinate where the UI element will be drawn
     * @param cost the cost of the tower 
     */
    public TowerUI(int towerType, float x, float y, int cost){
        this.texture = new Texture("TowerList/tower"+towerType+".png");
        this.x = x;
        this.y = y;
        this.towerType = towerType;
        this.cost = cost;
    }
    /**
     * Renders tower UI element, including the background, cost, and tower image.
     * @param batch the SpriteBatch used for rendering
     */
    public void draw(SpriteBatch batch){
        batch.draw(background, x, y, width, height);
        font.draw(batch, "Cost: " + cost, x + 5, y - 5);
        batch.draw(texture, x, y, width, height);
    }
    /**
     * Gets the bounding rectangle of the UI elements. 
     * @return a Rectangle representing the bounds of this UI element
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    /**
     * Gets the type identifier of the tower.  
     * @return the tower type 
     */
    public int getTowerType() {
        return towerType;
    }
    /**
     * Gets the x-coordinate of the UI element. 
     * @return the x-coordinate
     */
    public float getX() {return x;}

    /**
     * Gets the y-coordinate of the UI element.
     * @return the y-coordinate
     */
    public float getY() {return y;}

    /**
     * Disposes the texture and background. 
     */
    public void dispose(){
        texture.dispose();
        background.dispose();
    }
}
