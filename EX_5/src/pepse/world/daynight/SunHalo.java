package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.BackgroundUtils;

import java.awt.*;

/**
 * Represents the halo of sun.
 */
public class SunHalo {

    private static final String HALO_TAG = "sun";

    /**
     * This function creates a halo around a given object that represents the sun. The halo will be tied to
     * the given sun, and will always move with it.
     * @param gameObjects - The collection of all participating game objects.
     * @param layer - The number of the layer to which the created halo should be added.
     * @param sun - A game object representing the sun (it will be followed by the created game object).
     * @param color - The color of the halo.
     * @return A new game object representing the sun's halo.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color) {

        Renderable renderable = new OvalRenderable(color);

         GameObject halo = BackgroundUtils.addBackgroundObject(
                gameObjects,
                sun.getDimensions().mult(1.5f),
                Vector2.ZERO,
                renderable,
                layer,
                HALO_TAG);

        halo.addComponent((float deltaTime) -> halo.setCenter(sun.getCenter()));
        return halo;
    }

}
