package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.util.BackgroundUtils;

import java.awt.*;

/**
 * Represents the sky.
 */
public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    private static final String SKY_TAG = "sky";

    /**
     * This function creates a light blue rectangle which is always at the back of the window.
     * @param gameObjects - The collection of all participating game objects.
     * @param windowDimensions - The number of the layer to which the created game object should be added.
     * @param skyLayer - The number of the layer to which the created sky should be added.
     * @return A new game object representing the sky.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions, int skyLayer) {

        return BackgroundUtils.addBackgroundRectangle(gameObjects,
                windowDimensions,
                skyLayer,
                BASIC_SKY_COLOR,
                SKY_TAG);
    }
}
