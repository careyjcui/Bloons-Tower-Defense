package io.github.some_example_name;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Represents a Map in the game, containing a predefined track
 * and a series of rounds with different types of Bloons.
 * The Map can render its background texture and provides methods
 * to access the Bloons in each round.
 * 
 * @author Savir Singh
 * @author Charlie Qiu
 * @author Carey Cui
 * @version 2025/06/06
 */
public class Map {
    private static float[][] track;
    private ArrayList<Bloon>[] rounds;
    private Texture backgroundTexture;
    public static final float trackRadius = 25;

    /**
     * Constructs a new Map with a predefined track and background image.
     * Initializes the rounds with different types of Bloons.
     * 
     * @param track the coordinates of the track as a 2D float array
     * @param imagePath the path to the background image
     */
    public Map(float[][] track, String imagePath) {
        this.track = track;
        this.backgroundTexture = new Texture(imagePath);
        
        rounds = (ArrayList<Bloon>[]) new ArrayList[41];
        for (int i = 0; i < rounds.length; i++) {
            rounds[i] = new ArrayList<Bloon>();
        }

        //red <- blue <- green <- yellow <- pink <- black <- white <- lead <- rainbow <- ceramic 
        // Taken from https://static.wikia.nocookie.net/b__/images/6/64/RoundSummaryBTD6.png/revision/latest/scale-to-width-down/1000?cb=20211016202219&path-prefix=bloons

        // Round 1
        for (int i = 0; i < 20; i++) rounds[1].add(new Bloon(0, 0));

        // Round 2
        for (int i = 0; i < 40; i++) rounds[2].add(new Bloon(0, 0));
        for (int i = 0; i < 5; i++) rounds[2].add(new Bloon(1, 0));

        // Round 3
        for (int i = 0; i < 30; i++) rounds[3].add(new Bloon(0, 0));
        for (int i = 0; i < 10; i++) rounds[3].add(new Bloon(1, 0));

        // Round 4
        for (int i = 0; i < 30; i++) rounds[4].add(new Bloon(0, 0));
        for (int i = 0; i < 20; i++) rounds[4].add(new Bloon(1, 0));

        // Round 5
        for (int i = 0; i < 10; i++) rounds[5].add(new Bloon(0, 0));
        for (int i = 0; i < 30; i++) rounds[5].add(new Bloon(1, 0));
        for (int i = 0; i < 5; i++) rounds[5].add(new Bloon(2, 0));

        // Round 6
        for (int i = 0; i < 20; i++) rounds[6].add(new Bloon(0, 0));
        for (int i = 0; i < 20; i++) rounds[6].add(new Bloon(1, 0));
        for (int i = 0; i < 10; i++) rounds[6].add(new Bloon(2, 0));

        // Round 7
        for (int i = 0; i < 30; i++) rounds[7].add(new Bloon(0, 0));
        for (int i = 0; i < 30; i++) rounds[7].add(new Bloon(1, 0));
        for (int i = 0; i < 10; i++) rounds[7].add(new Bloon(2, 0));

        // Round 8
        for (int i = 0; i < 20; i++) rounds[8].add(new Bloon(0, 0));
        for (int i = 0; i < 30; i++) rounds[8].add(new Bloon(1, 0));
        for (int i = 0; i < 20; i++) rounds[8].add(new Bloon(2, 0));
        for (int i = 0; i < 5; i++) rounds[8].add(new Bloon(3, 0));

        // Round 9
        for (int i = 0; i < 40; i++) rounds[9].add(new Bloon(2, 0));
        for (int i = 0; i < 10; i++) rounds[9].add(new Bloon(3, 0));

        // Round 10
        for (int i = 0; i < 120; i++) rounds[10].add(new Bloon(1, 0));
        for (int i = 0; i < 10; i++) rounds[10].add(new Bloon(3, 0));

        // Round 11
        for (int i = 0; i < 20; i++) rounds[11].add(new Bloon(0, 0));
        for (int i = 0; i < 20; i++) rounds[11].add(new Bloon(1, 0));
        for (int i = 0; i < 25; i++) rounds[11].add(new Bloon(2, 0));
        for (int i = 0; i < 10; i++) rounds[11].add(new Bloon(3, 0));

        // Round 12
        for (int i = 0; i < 30; i++) rounds[12].add(new Bloon(1, 0));
        for (int i = 0; i < 20; i++) rounds[12].add(new Bloon(2, 0));
        for (int i = 0; i < 10; i++) rounds[12].add(new Bloon(3, 0));

        // Round 13
        for (int i = 0; i < 75; i++) rounds[13].add(new Bloon(1, 0));
        for (int i = 0; i < 50; i++) rounds[13].add(new Bloon(2, 0));

        // Round 14
        for (int i = 0; i < 75; i++) rounds[14].add(new Bloon(0, 0));
        for (int i = 0; i < 30; i++) rounds[14].add(new Bloon(1, 0));
        for (int i = 0; i < 20; i++) rounds[14].add(new Bloon(2, 0));
        for (int i = 0; i < 20; i++) rounds[14].add(new Bloon(3, 0));
        for (int i = 0; i < 5; i++) rounds[14].add(new Bloon(4, 0));

        // Round 15
        for (int i = 0; i < 40; i++) rounds[15].add(new Bloon(0, 0));
        for (int i = 0; i < 30; i++) rounds[15].add(new Bloon(1, 0));
        for (int i = 0; i < 25; i++) rounds[15].add(new Bloon(2, 0));
        for (int i = 0; i < 20; i++) rounds[15].add(new Bloon(3, 0));
        for (int i = 0; i < 10; i++) rounds[15].add(new Bloon(4, 0));

        // Round 16
        for (int i = 0; i < 60; i++) rounds[16].add(new Bloon(2, 0));
        for (int i = 0; i < 20; i++) rounds[16].add(new Bloon(3, 0));

        // Round 17
        for (int i = 0; i < 20; i++) rounds[17].add(new Bloon(3, 0));

        // Round 18
        for (int i = 0; i < 100; i++) rounds[18].add(new Bloon(2, 0));

        // Round 19
        for (int i = 0; i < 20; i++) rounds[19].add(new Bloon(2, 0));
        for (int i = 0; i < 20; i++) rounds[19].add(new Bloon(3, 0));
        for (int i = 0; i < 30; i++) rounds[19].add(new Bloon(4, 0));
        for (int i = 0; i < 5; i++) rounds[19].add(new Bloon(5, 0));

        // Round 20
        for (int i = 0; i < 10; i++) rounds[20].add(new Bloon(5, 0));

        // Round 21
        for (int i = 0; i < 60; i++) rounds[21].add(new Bloon(3, 0));
        for (int i = 0; i < 20; i++) rounds[21].add(new Bloon(4, 0));
        for (int i = 0; i < 10; i++) rounds[21].add(new Bloon(6, 0));

        // Round 22
        for (int i = 0; i < 30; i++) rounds[22].add(new Bloon(6, 0));

        // Round 23
        for (int i = 0; i < 10; i++) rounds[23].add(new Bloon(5, 0));
        for (int i = 0; i < 10; i++) rounds[23].add(new Bloon(6, 0));

        // Round 24
        for (int i = 0; i < 15; i++) rounds[24].add(new Bloon(4, 0));
        for (int i = 0; i < 20; i++) rounds[24].add(new Bloon(5, 0));
        for (int i = 0; i < 10; i++) rounds[24].add(new Bloon(6, 0));

        // Round 25
        for (int i = 0; i < 30; i++) rounds[25].add(new Bloon(3, 0));
        for (int i = 0; i < 25; i++) rounds[25].add(new Bloon(4, 0));
        for (int i = 0; i < 15; i++) rounds[25].add(new Bloon(5, 0));
        for (int i = 0; i < 5; i++) rounds[25].add(new Bloon(6, 0));
        
        // Round 26
        for (int i = 0; i < 40; i++) rounds[26].add(new Bloon(5, 0));
        for (int i = 0; i < 20; i++) rounds[26].add(new Bloon(6, 0));
        
        // Round 27
        for (int i = 0; i < 50; i++) rounds[27].add(new Bloon(4, 0));
        for (int i = 0; i < 25; i++) rounds[27].add(new Bloon(5, 0));
        for (int i = 0; i < 15; i++) rounds[27].add(new Bloon(6, 0));
        for (int i = 0; i < 5; i++) rounds[27].add(new Bloon(7, 0)); //lead
        
        // Round 28
        for (int i = 0; i < 25; i++) rounds[28].add(new Bloon(3, 0));
        for (int i = 0; i < 20; i++) rounds[28].add(new Bloon(4, 0));
        for (int i = 0; i < 30; i++) rounds[28].add(new Bloon(5, 0));
        for (int i = 0; i < 10; i++) rounds[28].add(new Bloon(6, 0));
        for (int i = 0; i < 10; i++) rounds[28].add(new Bloon(7, 0));
        
        // Round 29
        for (int i = 0; i < 50; i++) rounds[29].add(new Bloon(5, 0));
        for (int i = 0; i < 20; i++) rounds[29].add(new Bloon(6, 0));
        for (int i = 0; i < 15; i++) rounds[29].add(new Bloon(7, 0));
        
        // Round 30
        for (int i = 0; i < 60; i++) rounds[30].add(new Bloon(6, 0));
        for (int i = 0; i < 20; i++) rounds[30].add(new Bloon(7, 0));
        for (int i = 0; i < 5; i++) rounds[30].add(new Bloon(8, 0)); //rainbow
        
        // Round 31
        for (int i = 0; i < 40; i++) rounds[31].add(new Bloon(5, 0));
        for (int i = 0; i < 20; i++) rounds[31].add(new Bloon(6, 0));
        for (int i = 0; i < 15; i++) rounds[31].add(new Bloon(7, 0));
        for (int i = 0; i < 10; i++) rounds[31].add(new Bloon(8, 0));
        
        // Round 32
        for (int i = 0; i < 50; i++) rounds[32].add(new Bloon(6, 0));
        for (int i = 0; i < 25; i++) rounds[32].add(new Bloon(7, 0));
        for (int i = 0; i < 15; i++) rounds[32].add(new Bloon(8, 0));
        
        // Round 33
        for (int i = 0; i < 30; i++) rounds[33].add(new Bloon(6, 0));
        for (int i = 0; i < 30; i++) rounds[33].add(new Bloon(7, 0));
        for (int i = 0; i < 10; i++) rounds[33].add(new Bloon(8, 0));
        for (int i = 0; i < 5; i++) rounds[33].add(new Bloon(13, 0)); //ceramic
        
        // Round 34
        for (int i = 0; i < 20; i++) rounds[34].add(new Bloon(7, 0));
        for (int i = 0; i < 15; i++) rounds[34].add(new Bloon(8, 0));
        for (int i = 0; i < 10; i++) rounds[34].add(new Bloon(13, 0));
        
        // Round 35
        for (int i = 0; i < 30; i++) rounds[35].add(new Bloon(7, 0));
        for (int i = 0; i < 15; i++) rounds[35].add(new Bloon(8, 0));
        for (int i = 0; i < 15; i++) rounds[35].add(new Bloon(13, 0));
        
        // Round 36
        for (int i = 0; i < 50; i++) rounds[36].add(new Bloon(7, 0));
        for (int i = 0; i < 25; i++) rounds[36].add(new Bloon(8, 0));
        for (int i = 0; i < 15; i++) rounds[36].add(new Bloon(13, 0));
        
        // Round 37
        for (int i = 0; i < 40; i++) rounds[37].add(new Bloon(8, 0));
        for (int i = 0; i < 20; i++) rounds[37].add(new Bloon(13, 0));
        
        // Round 38
        for (int i = 0; i < 30; i++) rounds[38].add(new Bloon(7, 0));
        for (int i = 0; i < 25; i++) rounds[38].add(new Bloon(8, 0));
        for (int i = 0; i < 25; i++) rounds[38].add(new Bloon(13, 0));
        
        // Round 39
        for (int i = 0; i < 60; i++) rounds[39].add(new Bloon(8, 0));
        for (int i = 0; i < 30; i++) rounds[39].add(new Bloon(13, 0));
        
        // Round 40
        for (int i = 0; i < 10; i++) rounds[40].add(new Bloon(13, 0));
        for (int i = 0; i < 5; i++) rounds[40].add(new Bloon(13, 0));
        for (int i = 0; i < 5; i++) rounds[40].add(new Bloon(13, 0));

    }

    /**
     * Returns the rounds of Bloons in the Map.
     * Each round is represented as an ArrayList of Bloons.
     * 
     * @return an array of ArrayLists, where each ArrayList contains Bloons for that round
     */
    public ArrayList<Bloon>[] getRounds() {
        return rounds;
    }
    /**
     * Returns the background texture of the Map.
     * 
     * @return the Texture object representing the background
     */
    public Texture getTexture() {
        return backgroundTexture;
    }

    /**
     * Renders the background texture of the Map using the provided SpriteBatch.
     * The background is scaled to fit the screen width while maintaining its aspect ratio.
     * 
     * @param batch the SpriteBatch used for rendering
     */
    public void render(SpriteBatch batch) {
        int screenW = Gdx.graphics.getWidth();
        int screenH = Gdx.graphics.getHeight();
        float scale = (float) screenW / backgroundTexture.getWidth();
        float scaledH = backgroundTexture.getHeight() * scale;
        float y = (screenH - scaledH) / 2f;
        batch.draw(backgroundTexture,0, y,screenW, scaledH);
    }

    /**
     * Disposes of the background texture to free up resources.
     */
    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }

    /**
     * Returns the raw track coordinates as a 2D float array.
     * Each sub-array contains the x and y coordinates of a point on the track.
     * 
     * @return a 2D float array representing the track
     */
    public float[][] getRawList() {
        return track;
    }

    /**
     * Returns a list of points along the track, where each point is represented as a float array.
     * The points are calculated by interpolating between the track coordinates.
     * 
     * @return an ArrayList of float arrays, where each float array contains the x and y coordinates of a point
     */
    private ArrayList<float[]> getList() {
        ArrayList<float[]> pts = new ArrayList<>();

        for (int i = 0; i<track.length-1; i++) {
            float x1 = track[i][0];
            float y1 = track[i][1];
            float x2 = track[i+1][0];
            float y2 = track[i +1][1];

            int dx = (int) Math.abs(x2 - x1);
            int dy=(int) Math.abs(y2 - y1);

            int steps = Math.max(dx, dy);
            if (steps == 0) steps = 1;

            float xInc=(x2 - x1)/steps;
            float yInc = (y2 - y1) / steps;

            float x = x1;
            float y=y1;

            for (int j = 0; j <= steps; j++) {
                if (pts.size() == 0) {
                    pts.add(new float[]{Math.round(x), Math.round(y)});
                }
                if (pts.size() > 0 && !(Math.round(x) == pts.get(pts.size()-1)[0] && Math.round(y) == pts.get(pts.size()-1)[1])) {
                    pts.add(new float[]{Math.round(x), Math.round(y)});
                }
                x += xInc;
                y += yInc;
            }
        }

        return pts;
    }

    /**
     * Returns the position of a point on the track at the specified index.
     * The index corresponds to the position in the list of points along the track.
     * 
     * @param ind the index of the point on the track
     * @return a float array containing the x and y coordinates of the point
     */
    public float[] getPos(int ind) {
        return getList().get(ind);
    }

    /**
     * Returns the total number of points in the track.
     * This is equivalent to the length of the list of points along the track.
     * 
     * @return the number of points in the track
     */
    public int getLength() {
        return getList().size();
    }
}
