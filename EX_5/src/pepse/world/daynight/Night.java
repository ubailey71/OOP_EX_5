package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.BackgroundUtils;

import java.awt.*;

public class Night {
    private static final Color BASIC_NIGHT_COLOR = Color.black;
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final String NIGHT_TAG = "night";

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

