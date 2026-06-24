package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A subclass of Tower that represents a Tack Shooter, which can shoot 8 tacks at once.
 * 
 * @author Carey Cui
 * @version 2025/06/05
 */
public class TackShooter extends Tower {
    
    /**
     * Returns a TackShooter object at the specified location.
     * 
     * @param x the x coordinate of the center of the TackShooter, in pixels
     * @param y the y coordinate of the center of the TackShooter, in pixels
     */
    public TackShooter(float x, float y) {
        super(x, y, Bloon.bloonRadius, 150, 60, 1, 1, true, 0, 220);
        towerImage = new Texture("IngameTowers/tackShooter.png");
        projectileImage = new Texture("projectiles/dart.png");
    }

    @Override
    public void draw(SpriteBatch batch) {
        //draw tower
        batch.draw(towerImage, x - width / 2, y - height / 2, width, height);

        //draw projectiles
        for (Projectile projectile: projectiles) {
            batch.draw(projectileImage, projectile.getXPos(), projectile.getYPos(), projectile.getSize() / 2, projectile.getSize() / 2, projectile.getSize(), projectile.getSize(), 1, 1, projectile.getRotation(), 0, 0, projectile.getWidth(), projectile.getHeight(), false, false);
        }
    }
}
