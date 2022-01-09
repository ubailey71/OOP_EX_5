package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.BackgroundUtils;

import java.awt.*;

/**
 * Represents the sun - moves across the sky in an elliptical path.
 */
public class Sun {
    private static final Color SUN_COLOR = Color.YELLOW;
    private static final String SUN_TAG = "sun";
    private static final Vector2 SUN_DIMENSIONS = new Vector2(100, 100);

    /**
     * This function creates a yellow circle that moves in the sky in an elliptical path (in camera
     * coordinates).
     *
     * @param gameObjects      - The collection of all participating game objects.
     * @param layer            - The number of the layer to which the created sun should be added.
     * @param windowDimensions - The dimensions of the windows.
     * @param cycleLength      - The amount of seconds it should take the created game object to complete a
     *                        full cycle.
     * @return A new game object representing the sun.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {

        Renderable sunRenderable = new OvalRenderable(SUN_COLOR);

        GameObject sunGameObject = BackgroundUtils.addBackgroundObject(
                gameObjects,
                SUN_DIMENSIONS,
                Vector2.ZERO,
                sunRenderable,
                layer,
                SUN_TAG);

        Vector2 windowCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);

        //TODO: make floats constants, the float declaration in the callback is not needed.
        //TODO: check if the path of the sun in a regular circle or an oval.

        Transition<Float> locationChanger = new Transition<>(
                sunGameObject,
                (Float angle) -> sunGameObject.setCenter(calcSunCenter(angle, windowCenter)),
                0f,
                360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

        return sunGameObject;
    }

    private static Vector2 calcSunCenter(Float angle, Vector2 windowCenter) {
        float radianAngle = angle * ((float) Math.PI / 180) - 90;
        float radius = windowCenter.x() / 2;
        float x = windowCenter.x() + (radius * 1.5f * (float) Math.cos(radianAngle));
        float y = windowCenter.y() + (radius * (float) Math.sin(radianAngle));
        return new Vector2(x, y);
    }

}
