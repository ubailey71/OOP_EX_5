package pepse.util;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class BackgroundUtils {

    public static GameObject addBackgroundRectangle(GameObjectCollection gameObjects,
                                                    Vector2 windowDimensions,
                                                    int layer,
                                                    Color color) {

        Renderable backgroundRectangle = new RectangleRenderable(color);
        GameObject backgroundGameObject = new GameObject(Vector2.ZERO, windowDimensions, backgroundRectangle);
        backgroundGameObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(backgroundGameObject, layer);
        return backgroundGameObject;

    }

    public static GameObject addBackgroundRectangle(GameObjectCollection gameObjects,
                                                    Vector2 windowDimensions,
                                                    int layer,
                                                    Color color,
                                                    String tag) {

        GameObject backgroundGameObject = addBackgroundRectangle(gameObjects, windowDimensions, layer, color);
        backgroundGameObject.setTag(tag);
        return backgroundGameObject;

    }
}
