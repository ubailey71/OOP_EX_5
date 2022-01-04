package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.awt.*;
import pepse.world.trees.Tree;

public class PepseGameManager extends GameManager {


    private static final float CYCLE_LENGTH = 60;
    private static final Color haloColor = new Color(255, 255, 0, 20);
    private static final int GROUND_SURFACE_LAYER = Layer.STATIC_OBJECTS;
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS + 1;
    private static final int TREES_LAYER = Layer.STATIC_OBJECTS + 1;
    //TODO: remember to make order between the different layers

    //TODO: remove constructor when done.
    public PepseGameManager(String title, Vector2 windowDimensions) {
        super(title, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Sky.create(
                this.gameObjects(),
                windowController.getWindowDimensions(),
                Layer.BACKGROUND);

        Night.create(
                this.gameObjects(),
                Layer.FOREGROUND,
                windowController.getWindowDimensions(),
                CYCLE_LENGTH);

        GameObject sun = Sun.create(
                this.gameObjects(),
                Layer.BACKGROUND + 1,
                windowController.getWindowDimensions(),
                CYCLE_LENGTH);

        SunHalo.create(
                this.gameObjects(),
                Layer.BACKGROUND + 10,
                sun,
                haloColor);

        //TODO: make seed permanent.
        //terrain part
        int seed = 3;
        Vector2 windowDimensions = windowController.getWindowDimensions();
        Terrain terrain = new Terrain(gameObjects(), GROUND_SURFACE_LAYER, windowDimensions, seed);
        terrain.createInRange(0, (int) windowDimensions.x());

        //trees part
        Tree tree = new Tree(gameObjects(), terrain::groundHeightAt, Layer.BACKGROUND + 2, seed);
        tree.createInRange(0, (int) windowDimensions.x());



        //avatar creation
        Vector2 avatarLocation = windowDimensions.mult(0.2f);
        Avatar avatar = Avatar.create(gameObjects(), Layer.DEFAULT,avatarLocation, inputListener,
                imageReader);
    }

    public static void main(String[] args) {
        new PepseGameManager("Pepse", new Vector2(1200, 700)).run();
    }
}
