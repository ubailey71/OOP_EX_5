package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.BackgroundUtils;


import java.awt.*;


public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    private static final String SKY_TAG = "sky";

    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions, int skyLayer) {

        return BackgroundUtils.addBackgroundRectangle(gameObjects,
                windowDimensions,
                skyLayer,
                BASIC_SKY_COLOR,
                SKY_TAG);
    }
}
