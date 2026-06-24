package io.github.some_example_name;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Represents a Bloon (balloon enemy) in the game.
 * Each Bloon has a hit point (hp) value, position on the map,
 * and moves along a predefined path. When damaged, it can play
 * a pop sound and be disposed of if its hp drops below zero.
 * 
 * @author Charlie Qiu
 * @author Albert Hwang
 * @author Carey Cui
 * @version 2025/06/06
 */
public class Bloon {
    public Texture texture;
    public static Sound popSound;
    private int hp;
    private int pos;
    private int[] speed = {2,3,4,6,7,4,4,2,4,4,4,4,4,4};
    public static final float bloonRadius = 30;
    
    /**
     * Constructs a new Bloon.
     * 
     * @param hp the starting hp of the Bloon
     * @param pos the starting position index on the map
     */
    public Bloon(int hp, int pos){
        this.hp = hp;
        this.pos = pos;
        this.texture = new Texture("bloons/bloon"+hp+".png");

    }

    /**
     * Update the Bloon's position along the path based on its speed.
     */
    public void updatePos(){
        pos += speed[hp];
    }

    /**
     * Update the bloon's hp when it takes damage.
     * If hp goes below 0, the Bloon is considered popped and disposed. 
     * Pop sound is played when hp goes below 0.
     * @param damage the amount of damage taken by bloon
     */
    public void updateHp(int damage){
        this.hp -= damage;
        if(this.hp <= -1){
            if(popSound != null){
                popSound.play(0.3f);
            }
            this.dispose();
        }
        else{
            this.texture = new Texture("bloons/bloon"+hp+".png");
        }
    }
    
    /**
     * Checks whether the Bloon has reached the end of the path.
     * @param map the game map used to deteremine the length
     * @return true if the Bloon reached the end, false otherwise
     */
    public boolean endPoint(Map map){
        return pos >= map.getLength();
    }

    /**
     * Renders the Bloon on the screen using a SpriteBatch.
     * @param batch the SpriteBatch used to draw
     * @param map the map used to get Bloon's coordinates 
     */
    public void draw(SpriteBatch batch, Map map){
        batch.draw(texture, map.getPos(pos)[0] - getWidth() / 2, map.getPos(pos)[1] - getHeight() / 2, getWidth(), getHeight());
    }
    
    /**
     * Gets the current hp of the Bloon.
     * @return the Bloon's hp
     */
    public int getHp() {return hp;}

     /**
     * Gets the current position index of the Bloon on the path. 
     * @return the Bloon's path position
     */
    public int getPos() {return pos;}

     /**
     * Gets the width of the Bloon's texture. 
     * @return the width in pixels
     */
    public int getWidth() {return 60;}

     /**
     * Gets the height of the Bloon's texture. 
     * @return the heigth in pixels 
     */
    public int getHeight() {return 80;}

    /**
     * Disposes the Bloon's texture. 
     */
    public void dispose(){
        texture.dispose();
    }
}
