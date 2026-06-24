package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;

/**
 * A subclass of Tower that represents a Dart Monkey, which shoots darts at the Bloons.
 * 
 * @author Carey Cui
 * @version 2025/06/05
 */
public class DartMonkey extends Tower {

    /**
     * Returns a DartMonkey object at the specified location.
     * 
     * @param x the x coordinate of the center of the DartMonkey, in pixels
     * @param y the y coordinate of the center of the DartMonkey, in pixels
     */
    public DartMonkey(float x, float y) {
        super(x, y, Bloon.bloonRadius, 250, 30, 1, 1, false, 0, 170);
        towerImage = new Texture("IngameTowers/bottomDart.png");
        projectileImage = new Texture("projectiles/dart.png");
    }
}
