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
import pepse.util.ScreenChunkManager;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.avatarProperties.AvatarKeymap;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PepseGameManager extends GameManager {


    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private static final float AVATAR_POSITION_FACTOR = 0.2f;
    private static final float CYCLE_LENGTH = 60;
    private static final int SEED = 3;

    //Layers
    public static final int SKY_LAYER = Layer.BACKGROUND;
    public static final int SUN_LAYER = Layer.BACKGROUND + 1;
    public static final int TREES_LAYER = Layer.BACKGROUND + 2;
    public static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    public static final int TERRAIN_SURFACE_LAYER = Layer.STATIC_OBJECTS;
    public static final int AVATAR_LAYER = Layer.DEFAULT;
    public static final int NIGHT_LAYER = Layer.FOREGROUND;

    private Avatar player1;
    private Avatar player2;
    private ScreenChunkManager screenChunkManager;

    /**
     * The method will be called once when a GameGUIComponent is created, and again after every
     * invocation of windowController.resetGame().
     *
     * @param imageReader      - Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      - Contains a single method: readSound, which reads a wav file from disk.
     *                         See its documentation for help.
     * @param inputListener    - Contains a single method: isKeyPressed, which returns whether a given key
     *                         is currently pressed by the user or not. See its documentation.
     * @param windowController - Contains an array of helpful, self-explanatory methods concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        // Initializing Sky, Sun, Sun halo and Night
        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, CYCLE_LENGTH);
        GameObject sun = Sun.create(gameObjects(), SUN_LAYER, windowDimensions, CYCLE_LENGTH);
        SunHalo.create(gameObjects(), SUN_HALO_LAYER, sun, HALO_COLOR);

        // Initializing Terrain and Trees
        Terrain terrain = new Terrain(gameObjects(), TERRAIN_SURFACE_LAYER, windowDimensions, SEED);
        Tree tree = new Tree(gameObjects(), terrain::groundHeightAt, TREES_LAYER, SEED);
        gameObjects().layers().shouldLayersCollide(Layer.STATIC_OBJECTS, Layer.DEFAULT, true);


        //avatar creation

        //player1 creation
        Vector2 avatarLocation = new Vector2(windowDimensions.x() / 2,
                terrain.groundHeightAt(windowDimensions.x()) / 2);
        String player1BasePath = "EX_5/src/pepse/assets/player1-frames\\knight-frame";
        String player1suffix = ".png";
        AvatarKeymap player1Keymap = new AvatarKeymap(KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT,
                KeyEvent.VK_SPACE, KeyEvent.VK_SHIFT);
        player1 = Avatar.create(gameObjects(), AVATAR_LAYER, avatarLocation, inputListener,
                imageReader, player1BasePath, player1suffix, player1Keymap);
        setCamera(new Camera(player1, Vector2.ZERO, windowDimensions, windowDimensions));
        this.screenChunkManager = new ScreenChunkManager(gameObjects(), windowDimensions, tree, terrain,
                avatarLocation.x());

//        player2 creation
        Vector2 player2Location = windowDimensions.mult(0.4f);
        String player2BasePath = "EX_5/src/pepse/assets/player2-frames\\player2-frame";
        String player2Suffix = ".png";
        AvatarKeymap player2Keymap = new AvatarKeymap(KeyEvent.VK_D, KeyEvent.VK_A,
                KeyEvent.VK_W, KeyEvent.VK_E);
        player2 = Avatar.create(gameObjects(), Layer.DEFAULT, player2Location, inputListener,
                imageReader, player2BasePath, player2Suffix, player2Keymap);

        this.setCamera(new Camera(Vector2.ZERO, windowDimensions, windowDimensions));

//        cameraManager
        CameraManager camManager = new CameraManager(this.camera(), player1, player2, inputListener,
                KeyEvent.VK_F);

        this.gameObjects().addGameObject(camManager);
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (player1 != null) {
            float player1XPosition = player1.getCenter().x();
            screenChunkManager.updateChunks(player1XPosition, player2.getCenter().x());
        }
    }
}
