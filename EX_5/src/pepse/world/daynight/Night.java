package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.Transition;
import danogl.util.Vector2;
import pepse.util.BackgroundUtils;

import java.awt.*;

/**
 * Darkens the entire window.
 */
public class Night {
    private static final Color BASIC_NIGHT_COLOR = Color.black;
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final String NIGHT_TAG = "night";

    /**
     * This function creates a black rectangular game object that covers the entire game window and changes
     * its opaqueness in a cyclic manner, in order to resemble day-to-night transitions.
     *
     * @param gameObjects      The collection of all participating game objects.
     * @param layer            The number of the layer to which the created game object should be added.
     * @param windowDimensions The dimensions of the windows.
     * @param cycleLength      The amount of seconds it should take the created game object to complete a
     *                         full cycle.
     * @return A new game object representing day-to-night transitions.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {

        GameObject night = BackgroundUtils.addBackgroundRectangle(
                gameObjects,
                windowDimensions,
                layer,
                BASIC_NIGHT_COLOR,
                NIGHT_TAG);

        //TODO: change 0 f to CONSTANT and also the dividing by 2!
        Transition<Float> opaquenessChanger = new Transition<>(
                night,
                night.renderer()::setOpaqueness,
                0f,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );

        return night;
    }
}

