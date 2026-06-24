package io.github.some_example_name;

/**
 * Map1 class represents a specific map layout for the BTD6 game.
 * It extends the Map class and initializes the track coordinates and image path.
 * The track is defined by a series of coordinates that outline the path.
 * The image path points to the visual representation of the map.
 * 
 * @author Savir Singh
 * @author Charlie Qiu
 * @version 2025/06/06
 */
public class Map1 extends Map {
    private static final float[][] TRACK = {
        {-30, 420}, {640, 420}, {640, 610}, {420, 610}, {420, 110}, {210, 110}, {210, 290}, {800, 290}, {800, 490}, {960, 490}, {960, 170}, {570, 170}, {570, -40}
    };

    private static final String IMAGE_PATH = "Maps/map1.png";

    /**
     * Constructor for Map1.
     * Initializes the map with predefined track coordinates and image path.
     */
    public Map1() {
        super(TRACK, IMAGE_PATH);
    }
    
}
