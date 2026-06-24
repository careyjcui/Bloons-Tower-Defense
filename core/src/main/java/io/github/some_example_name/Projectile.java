package io.github.some_example_name;
import com.badlogic.gdx.math.MathUtils;

/**
 * This class represents a projectile in the game with position, speed, damage, health, and size attributes.
 * 
 * @author Albert Hwang
 * @author Carey Cui
 * @version 2025/06/06
 */
public class Projectile {
    private float xPos;
    private float yPos;
    private float xSpeed;
    private float ySpeed;
    private int damage; 
    private int hp;
    private int width;
    private int height;
    private int size = 40;
    
     /**
     * Constructs a new Projectile object with specified attributes.
     *
     * @param xPos   The initial x-coordinate of the projectile.
     * @param yPos   The initial y-coordinate of the projectile.
     * @param xSpeed The speed of the projectile in the x direction.
     * @param ySpeed The speed of the projectile in the y direction.
     * @param damage The damage the projectile deals on the bloons.
     * @param hp     The health of the projectile.
     * @param width  The width of the projectile's hitbox.
     * @param height The height of the projectile's hitbox.
     */
    public Projectile(float xPos, float yPos, float xSpeed, float ySpeed, int damage, int hp, int width, int height) {                
        this.xPos = xPos;
        this.yPos = yPos;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed; 
        this.damage = damage;
        this.hp = hp;
        this.width = width;
        this.height = height;
    }
    /**
     * Gets the x position of this projectile, in pixels.
     * @return The current x-coordinate of the projectile.
     */   
    public float getXPos(){
        return xPos;
    }
    /**
     * Gets the y position of this projectile, in pixels.
     * @return The current y-coordinate of the projectile.
     */ 
    public float getYPos(){
        return yPos;
    }
    /**
     * Gets the damage of this projectile.
     * @return The damage this projectile deals. 
     */
    public int getDamage(){
        return damage;
    }
    /**
     * Gets the current hp of this projectile.
     * @return The current health of the projectile.
     */
    public int getHp(){
        return hp;
    }
     /**
     * Gets the width of this projectile. 
     * @return The width of the projectile.
     */
    public int getWidth() {
        return width;
    }
    /**
     * Gets the height of this projectile.
     * @return The height of the projectile.
     */
    public int getHeight() {
        return height;
    }
    /**
     * Gets the height of this projectile. 
     * @return The size of the projectile. 
     */
    public int getSize(){
        return size;
    }
    /**
     * Calculates the rotation angle of the projectile in degrees based on its speed vector.
     * @return The angle in degrees the projectile is facing (0–360).
     */
    public float getRotation() {
        return MathUtils.atan2Deg360(ySpeed, xSpeed);
    }
     /**
     * Decreases the projectile's HP by 1.
     * @return The updated health after decrement.
     */
    public int hpDecrease(){
        hp -= 1;
        return hp;
    }
    /**
     * Updates the projectile's position based on its speed.
     */
    public void update(){
        xPos += xSpeed;
        yPos += ySpeed;
    }

    /**
     * Checks if the projectile has moved out of the screen bounds.
     * @return true if the projectile is outside the 1280x720 screen, false otherwise.
     */ 
    public boolean outOfMap(){
        if(xPos >= 1280 || xPos <= 0 || yPos >= 720 || yPos <=0){
            return true;
        }
        else{
            return false;
        }
    }
}  
