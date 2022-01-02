package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.BackgroundUtils;

import java.awt.*;

public class SunHalo {

    private static final String HALO_TAG = "sun";

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
