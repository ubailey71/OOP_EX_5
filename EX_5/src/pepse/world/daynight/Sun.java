package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    private static final Color SUN_COLOR = Color.YELLOW;
    private static final String SUN_TAG = "sun";
    private static final Vector2 SUN_DIMENSIONS = new Vector2(100,100);
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){

        Renderable sunRenderable = new OvalRenderable(SUN_COLOR);
        GameObject sunGameObject = new GameObject(Vector2.ZERO, SUN_DIMENSIONS,sunRenderable);
        sunGameObject.setTag(SUN_TAG);

        sunGameObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        Vector2 windowCenter = new Vector2(windowDimensions.x()/2, windowDimensions.y()/2);

        Transition<Float> locationChanger = new Transition<>(
                sunGameObject,
                 (Float angle) -> sunGameObject.setCenter(calcSunCenter(angle,windowCenter)),
                0f,
                360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

        gameObjects.addGameObject(sunGameObject,layer);
        return sunGameObject;
    }

    private static Vector2 calcSunCenter (Float angle,Vector2 windowCenter){
        float radianAngle = angle * ((float) Math.PI / 180) - 90;
        float radius = windowCenter.x() / 2;
        float x = windowCenter.x() + (radius * 1.5f * (float)Math.cos(radianAngle));
        float y = windowCenter.y() + (radius * (float)Math.sin(radianAngle));
        return new Vector2(x,y);
    }

}
