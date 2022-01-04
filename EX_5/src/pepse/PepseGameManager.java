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
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.avatarProperties.AvatarKeymap;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.awt.*;
import java.awt.event.KeyEvent;
import pepse.world.trees.Tree;

/**
 * The main class of the simulator.
 */
public class PepseGameManager extends GameManager {

    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private static final float AVATAR_POSITION_FACTOR = 0.2f;
    private static final float CYCLE_LENGTH = 60;
    private static final int SEED = 3;

    //Layers
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int TREES_LAYER = Layer.BACKGROUND + 2;
    private static final int LEAVES_LAYER = Layer.BACKGROUND + 3;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    private static final int TERRAIN_SURFACE_LAYER = Layer.STATIC_OBJECTS;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS + 1;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;


    private Vector2 windowDimensions;
    private Avatar avatar = null;
    private Terrain terrain = null;
    private Tree tree = null;
    private ScreenChunkManager chunkManager;

    private int curLeftX = 0;
    private int curRightX = 0;

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
        windowDimensions = windowController.getWindowDimensions();

        // Initializing Sky, Sun, Sun halo and Night
        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, CYCLE_LENGTH);
        GameObject sun = Sun.create(gameObjects(), SUN_LAYER, windowDimensions, CYCLE_LENGTH);
        SunHalo.create(gameObjects(), SUN_HALO_LAYER, sun, HALO_COLOR);


        // Initializing Terrain and Trees
        curRightX = (int) windowDimensions.x();
        terrain = new Terrain(gameObjects(), TERRAIN_SURFACE_LAYER, windowDimensions, SEED);
        terrain.createInRange(0, (int) windowDimensions.x());

        tree = new Tree(gameObjects(), terrain::groundHeightAt, TREES_LAYER, SEED);
        tree.createInRange(0, (int) windowDimensions.x());

        gameObjects().layers().shouldLayersCollide(Layer.STATIC_OBJECTS, Layer.DEFAULT, true);

        //Initializing Avatar
        Vector2 avatarPos = windowDimensions.mult(AVATAR_POSITION_FACTOR);
        avatar = Avatar.create(gameObjects(), AVATAR_LAYER, avatarPos, inputListener, imageReader);

        setCamera(new Camera(avatar, Vector2.ZERO, windowDimensions, windowDimensions));

        chunkManager = new ScreenChunkManager(gameObjects(), windowDimensions, terrain::createInRange,
                tree::createInRange);


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

    /**
     * Runs the entire simulation.
     * @param args - This argument should not be used.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (avatar != null){
            float avatarXPosition = avatar.getCenter().x();
            chunkManager.updateChunks(avatarXPosition);
        }
    }
}
