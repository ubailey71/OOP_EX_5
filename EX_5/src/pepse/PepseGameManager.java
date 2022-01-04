package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.util.CameraManager;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.avatarProperties.AvatarKeymap;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.awt.*;
import java.awt.event.KeyEvent;
import pepse.world.trees.Tree;

public class PepseGameManager extends GameManager {


    private static final float CYCLE_LENGTH = 60;
    private static final Color haloColor = new Color(255, 255, 0, 20);
    private static final int GROUND_SURFACE_LAYER = Layer.STATIC_OBJECTS;
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS + 1;
    private static final int TREES_LAYER = Layer.STATIC_OBJECTS + 1;
    //TODO: remember to make order between the different layers

    @Override public void initializeGame(ImageReader imageReader, SoundReader soundReader,
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
        //player1 creation
        Vector2 avatarLocation = windowDimensions.mult(0.2f);
        String player1BasePath = "EX_5/src/pepse/assets/player1-frames\\knight-frame";
        String player1suffix = ".png";
        AvatarKeymap player1Keymap = new AvatarKeymap(KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT,
                KeyEvent.VK_SPACE, KeyEvent.VK_SHIFT);
        Avatar player1 = Avatar.create(gameObjects(), Layer.DEFAULT, avatarLocation, inputListener,
                imageReader, player1BasePath, player1suffix, player1Keymap);

        //player2 creation
        Vector2 player2Location = windowDimensions.mult(0.4f);
        String player2BasePath = "EX_5/src/pepse/assets/player2-frames\\player2-frame";
        String player2Suffix = ".png";
        AvatarKeymap player2Keymap = new AvatarKeymap(KeyEvent.VK_D, KeyEvent.VK_A,
                KeyEvent.VK_W, KeyEvent.VK_E);
        Avatar player2 = Avatar.create(gameObjects(), Layer.DEFAULT, player2Location, inputListener,
                imageReader, player2BasePath, player2Suffix, player2Keymap);

        this.setCamera(new Camera(Vector2.ZERO, windowDimensions, windowDimensions));
        //cameraManager
        CameraManager camManager = new CameraManager(this.camera(), player1, player2, inputListener,
                KeyEvent.VK_F);

        this.gameObjects().addGameObject(camManager);
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
