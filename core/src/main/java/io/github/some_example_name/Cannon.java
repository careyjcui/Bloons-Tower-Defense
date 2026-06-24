package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;

/**
 * A subclass of Tower that represents a Cannon, which can shoot explosive bombs.
 * 
 * @author Carey Cui
 * @author Charlie Qiu
 * @version 2025/06/05
 */
public class Cannon extends Tower {
    
    /**
     * Returns a Cannon object at the specified location.
     * 
     * @param x the x coordinate of the center of the Cannon, in pixels
     * @param y the y coordinate of the center of the Cannon, in pixels
     */
    public Cannon(float x, float y) {
        super(x, y, Bloon.bloonRadius, 250, 90, 1, 1, false, 80, 320);
        towerImage = new Texture("IngameTowers/botCannon.png");
        projectileImage = new Texture("projectiles/bomb.png");
    }

    /**
     * Increases the explosionRadius of this Cannon by a factor of 1.5.
     * <p>
     * This method overrides upgradePiercing in Tower because it does not make sense for a bomb to pierce through Bloons.
     * Instead of increasing projectileHP, the area of effect of the bomb is increased.
     */
    @Override
    public void upgrade() {
        upgradeLevel++;
        if (upgradeLevel == 0) {
            range *= 1.5;
        }
        else if (upgradeLevel == 1) {
            damage++;
        }
        else if (upgradeLevel == 2) {
            explosionRadius *= 1.5;
        }
    }
}
