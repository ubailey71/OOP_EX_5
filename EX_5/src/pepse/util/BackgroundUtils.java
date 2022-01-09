package pepse.util;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * a class for generating background objects (such as rectangle and circle etc. )
 */
public class BackgroundUtils {
    /**
     * adds a rectangle to the background
     * @param gameObjects the GameObjectCollection of the game
     * @param windowDimensions the dimensions of the window
     * @param layer the layer in which to add the objects
     * @param color the color of the rectangle
     * @return the GameObject created
     */
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
    /**
     * adds a rectangle to the background with a tag
     * @param gameObjects the GameObjectCollection of the game
     * @param windowDimensions the dimensions of the window
     * @param layer the layer in which to add the objects
     * @param color the color of the rectangle
     * @param tag a tag to add to the GameObject
     * @return the GameObject created
     */
    public static GameObject addBackgroundRectangle(GameObjectCollection gameObjects,
                                                    Vector2 windowDimensions,
                                                    int layer,
                                                    Color color,
                                                    String tag) {

        GameObject backgroundGameObject = addBackgroundRectangle(gameObjects, windowDimensions, layer, color);
        backgroundGameObject.setTag(tag);
        return backgroundGameObject;

    }

    /**
     * adds a generic gameObject to the background
     * @param gameObjects the GameObjectCollection of the game
     * @param location  coordinates for the object
     * @param layer the layer in which to add the objects
     * @param renderable the renderable of the object
     * @return the GameObject created
     */
    public static GameObject addBackgroundObject(GameObjectCollection gameObjects,
                                                 Vector2 size,
                                                 Vector2 location,
                                                 Renderable renderable,
                                                 int layer) {
        GameObject gameObject = new GameObject(Vector2.ZERO, size, renderable);
        gameObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObject.setCenter(location);
        gameObjects.addGameObject(gameObject, layer);
        return gameObject;
    }

    /**
     * adds a generic gameObject to the background with a tag
     * @param gameObjects the GameObjectCollection of the game
     * @param location  coordinates for the object
     * @param layer the layer in which to add the objects
     * @param renderable the renderable of the object
     * @param tag a tag to add to the GameObject
     * @return the GameObject created
     */
    public static GameObject addBackgroundObject(GameObjectCollection gameObjects,
                                                 Vector2 size,
                                                 Vector2 location,
                                                 Renderable renderable,
                                                 int layer,
                                                 String tag) {
        GameObject gameObject = addBackgroundObject(gameObjects, size, location, renderable, layer);
        gameObject.setTag(tag);
        return gameObject;
    }
}
