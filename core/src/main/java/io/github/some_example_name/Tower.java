package io.github.some_example_name;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The parent class used to represent a Tower, which shoots at the bloons inside its range.
 * 
 * @author Carey Cui
 * @author Charlie Qiu
 * @version 2025/06/05
 */
public class Tower {
    
    protected Texture towerImage;
    protected Texture projectileImage;

    protected float x;
    protected float y;
    protected static final int width = 180;
    protected static final int height = 180;
    private float radius;
    protected float range;
    private int cooldown;
    protected int damage;
    private int projectileHP;
    private float angle;
    private boolean multishot;
    protected float explosionRadius;
    private int cost;
    public static Sound explosion;
    protected int upgradeLevel = 0;

    protected Array<Projectile> projectiles = new Array<>();

    /**
     * Returns a Tower object with the specified attributes.
     * 
     * @param x the x coordinate of the center of the Tower, in pixels
     * @param y the y coordinate of the center of the Tower, in pixels
     * @param radius the radius of the Tower, in pixels
     * @param range the radius of the circular range of the Tower, in pixels
     * @param cooldown the number of frames it takes this Tower to shoot
     * @param damage the amount of HP deducted from Bloons that collide with this Tower's projectiles
     * @param projectileHP the number of Bloons a single projectile of this Tower can hit
     * @param multishot true if this Tower is a Tack Shooter, false otherwise
     * @param explosionRadius the radius of the circular area of effect of this Tower's projectiles, non-explosive projectiles have explosionRadius of 0
     * @param cost the cost to build this Tower
     */
    protected Tower(float x, float y, float radius, float range, int cooldown, int damage, int projectileHP, boolean multishot, float explosionRadius, int cost) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.range = range;
        this.cooldown = cooldown;
        this.damage = damage;
        this.projectileHP = projectileHP;
        this.multishot = multishot;
        this.explosionRadius = explosionRadius;
        this.cost = cost;
    }

    /**
     * Returns the amount of money earned by this Tower's projectiles after handling all game logic.
     * 
     * @param frameCount the number of frames that have passed since the current round began
     * @param bloons an Array containing all Bloons that are alive
     * @param map the Map on which the game is being played
     * @return the money earned from this Tower's projectiles popping Bloons
     */
    public int render(int frameCount, Array<Bloon> bloons, Map map) {
        //shoot projectile if not in cooldown
        if (frameCount % cooldown == 0) {
            //iterate alive bloons to check for shooting
            for (Bloon bloon: bloons) {
                float bloonX = map.getPos(bloon.getPos())[0];
                float bloonY = map.getPos(bloon.getPos())[1];
                //check if the bloon is in range of the tower
                if (Math.sqrt(Math.pow(bloonX - x, 2) + Math.pow(bloonY - y, 2)) <= range) {
                    //check if the tack shooter is shooting
                    if (multishot) {
                        //shoot 8 projectiles
                        for (int i = 0; i < 8; i++) {
                            float angle = i * MathUtils.PI / 4;
                            Projectile projectile = new Projectile(x, y, Bloon.bloonRadius * MathUtils.cos(angle), Bloon.bloonRadius * MathUtils.sin(angle), damage, projectileHP, 623, 401);
                            projectiles.add(projectile);
                        }
                    } else {
                        //shoot 1 projectile
                        Projectile projectile = null;
                        angle = MathUtils.atan2Deg(bloonY - y, bloonX - x);
                        //check if the projectile is explosive
                        if (explosionRadius == 0) {
                            //shoot non-explosive projectile
                            projectile = new Projectile(x, y, Bloon.bloonRadius * MathUtils.cosDeg(angle), Bloon.bloonRadius * MathUtils.sinDeg(angle), damage, projectileHP, 623, 401);
                        } else {
                            //shoot explosive projectile
                            projectile = new Projectile(x, y, Bloon.bloonRadius * MathUtils.cosDeg(angle), Bloon.bloonRadius * MathUtils.sinDeg(angle), damage, projectileHP, 529, 471);
                        }
                        projectiles.add(projectile);
                    }
                    //prevent more shooting
                    break;
                }
            }
        }

        int money = 0;
        //move projectiles and check collision with bloons
        for (Projectile projectile: projectiles) {
            projectile.update();

            //pre-compute list of bloons that may be damaged by explosion
            Array<Bloon> inExplosion = new Array<>();
            if (explosionRadius > 0) {
                for (Bloon bloon: bloons) {
                    float bloonX = map.getPos(bloon.getPos())[0];
                    float bloonY = map.getPos(bloon.getPos())[1];
                    float projectileX = projectile.getXPos();
                    float projectileY = projectile.getYPos();
                    //add bloon to list if it is in explosion radius and it is not black
                    if (Math.sqrt(Math.pow(bloonX - projectileX, 2) + Math.pow(bloonY - projectileY, 2)) <= explosionRadius && bloon.getHp() != 5) {
                        inExplosion.add(bloon);
                    }
                }
            }

            //iterate alive bloons to check for collision
            for (Bloon bloon: bloons) {
                float bloonX = map.getPos(bloon.getPos())[0];
                float bloonY = map.getPos(bloon.getPos())[1];
                float projectileX = projectile.getXPos();
                float projectileY = projectile.getYPos();
                //collision detection against bloons
                if (Math.sqrt(Math.pow(bloonX - projectileX, 2) + Math.pow(bloonY - projectileY, 2)) <= Bloon.bloonRadius) {
                    if (explosionRadius == 0) {
                        //non-explosive projectiles
                        money += Math.min(projectile.getDamage(), bloon.getHp() + 1);
                        bloon.updateHp(projectile.getDamage());
                    } else {
                        //explosive projectiles
                        explosion.play(0.5f);
                        for (Bloon explodedBloon: inExplosion) {
                            money += Math.min(projectile.getDamage(), bloon.getHp() + 1);
                            explodedBloon.updateHp(projectile.getDamage());
                        }
                    }
                    projectile.hpDecrease();
                    //prevent projectile from popping more bloons
                    if (projectile.getHp() <= 0) {
                        break;
                    }
                }
            }

            //delete projectile if it has no hitpoints remaining or if it is out of bounds
            if (projectile.getHp() <= 0 || projectile.outOfMap()) {
                projectiles.removeValue(projectile, false);
            }
        }
        return money;
    }

    /**
     * Draws this Tower and its projectiles on the specified SpriteBatch.
     * 
     * @param batch the SpriteBatch to draw this Tower and its projectiles on
     */
    public void draw(SpriteBatch batch) {
        //draw tower
        batch.draw(towerImage, x - width / 2, y - height / 2, width / 2, height / 2, width, height, 1, 1, angle + 90, 0, 0, width, height, false, false);

        //draw projectiles
        for (Projectile projectile: projectiles) {
            batch.draw(projectileImage, projectile.getXPos(), projectile.getYPos(), projectile.getSize() / 2, projectile.getSize() / 2, projectile.getSize(), projectile.getSize(), 1, 1, projectile.getRotation(), 0, 0, projectile.getWidth(), projectile.getHeight(), false, false);
        }
    }

    /**
     * Returns whether or not this Tower is colliding with the track on the specified Map.
     * <p>
     * This method is only valid for maps that contain horizontal and vertical tracks.
     * The method does not return correct results when used on a map with slanted or curving tracks.
     * 
     * @param map a Map object that contains the track to check collision against
     * @return true if the tower is colliding with the track, false otherwise
     */
    public boolean collideTrack(Map map) {
        float[][] track = map.getRawList();
        float clearance = radius + Map.trackRadius;

        //compare tower against each segment of the track
        for (int i = 1; i < track.length; i++) {
            float x1 = Math.min(track[i - 1][0], track[i][0]);
            float x2 = Math.max(track[i - 1][0], track[i][0]);
            float y1 = Math.min(track[i - 1][1], track[i][1]);
            float y2 = Math.max(track[i - 1][1], track[i][1]);

            //track segment is horizontal
            if (y1 == y2) {
                //tower is on left side of track segment
                if (x < x1) {
                    //collision detection against (x1, y1)
                    if (Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2)) <= clearance) return true;
                }
                //tower is on right side of track segment
                else if (x > x2) {
                    //collision detection against (x2, y1)
                    if (Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y1, 2)) <= clearance) return true;
                }
                //tower is above or below track segment
                else {
                    //check vertical distance
                    if (Math.abs(y - y1) <= clearance) return true;
                }
            }

            //track segment is vertical
            else {
                //tower is above track segment
                if (y > y2) {
                    //collision detection against (x1, y2)
                    if (Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y2, 2)) <= clearance) return true;
                }
                //tower is below track segment
                else if (y < y1) {
                    //collision detection against (x1, y1)
                    if (Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2)) <= clearance) return true;
                }
                //tower is on left or right side of track segment
                else {
                    //check horizontal distance
                    if (Math.abs(x - x1) <= clearance) return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Returns a Texture that can be used to draw this Tower.
     * 
     * @return the Texture that represents this Tower
     */
    public Texture getImage() {
        return towerImage;
    }

    /**
     * Returns the x position of this Tower, in pixels.
     * 
     * @return the x coordinate of the center of this Tower
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the y position of this Tower, in pixels.
     * 
     * @return the y coordinate of the center of this Tower
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the radius of this Tower, in pixels.
     * 
     * @return the radius of the hitbox of this Tower
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Returns the radius of the circular range of this Tower, in pixels.
     * 
     * @return the range, or distance within which bloons can be targeted, of this Tower
     */
    public float getRange(){
        return range;
    }

    /**
     * Returns the cost to build this tower.
     * 
     * @return the cost of this Tower
     */
    public int getCost() {
        return cost;
    }

    /**
     * Returns the current upgrade level of this Tower.
     * 
     * @return the upgrade level of this Tower
     */
    public int getUpgradeLevel() {
        return upgradeLevel;
    }
    /**
     * Upgrades the tower when called apon.
     */
    public void upgrade() {
        if (upgradeLevel == 0) {
            range *= 1.5;
        }
        else if (upgradeLevel == 1) {
            damage++;
        }
        else if (upgradeLevel == 2) {
            projectileHP++;
        }
        upgradeLevel++;
    }
}