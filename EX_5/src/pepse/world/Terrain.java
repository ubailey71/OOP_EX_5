package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.PerlinNoise;
import pepse.util.ScreenChunkManager;

import java.awt.*;


/**
 * Responsible for the creation and management of terrain.
 */
public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final String GROUND_TAG = "ground";
    private static final float GROUND_PERCENTAGE = 2 / 3f;
    private static final int TOP_LAYERS_AMOUNT = 2;
    private static final int TERRAIN_DEPTH = 20;

    private final GameObjectCollection gameObjects;
    private final PerlinNoise perlinNoise;
    private final Vector2 windowDimensions;
    private final float groundHeightAtX0;
    private final int groundLayer;


    /**
     * Constructor.
     *
     * @param gameObjects      - The collection of all participating game objects.
     * @param groundLayer      - The number of the layer to which the created ground objects should be added.
     * @param windowDimensions - The dimensions of the windows.
     * @param seed             - A seed for a random number generator.
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = windowDimensions.y() * GROUND_PERCENTAGE;
        this.windowDimensions = windowDimensions;
        this.perlinNoise = new PerlinNoise(seed);
    }

    /**
     * This method return the ground height at a given location.
     *
     * @param x - A number.
     * @return The ground height at the given location.
     */
    public float groundHeightAt(float x) {
        float groundHeight = (float) perlinNoise.noise(x / Block.SIZE);
        return groundHeight * windowDimensions.y() / 2 + groundHeightAtX0;
    }

    /**
     * This method creates terrain in a given range of x-values.
     *
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        float blockSize = Block.SIZE;

        // X position of the first and last blocks
        float firstX = (float) (Math.ceil(Math.abs(minX) / blockSize) * (int) Math.signum(minX) * blockSize);
        float lastX = (float) (Math.ceil(Math.abs(maxX) / blockSize) * (int) Math.signum(maxX) * blockSize);

        float curXPosition = firstX;
        while (curXPosition <= lastX - blockSize) {
            float groundHeight = (float) Math.floor(groundHeightAt(curXPosition) / blockSize) * blockSize;

            for (int blockIdx = 0; blockIdx < TERRAIN_DEPTH; blockIdx++) {

                float curYPosition = groundHeight + blockIdx * blockSize;
                Vector2 blockPosition = new Vector2(curXPosition, curYPosition);
                createSingleBlock(gameObjects, blockPosition, blockIdx, groundLayer);
            }
            curXPosition += blockSize;
        }
    }

    /*
    creates a single block of terrain.
     */
    private static void createSingleBlock(GameObjectCollection gameObj, Vector2 topLeftCorner,
                                          int blockRowNum, int layer) {

        Renderable groundRender = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        Block block = new Block(topLeftCorner, groundRender);
        block.setTag(GROUND_TAG);

        // only TOP_LAYERS_AMOUNT of block rows will be able to collide with objects
        if (blockRowNum < TOP_LAYERS_AMOUNT) {
            gameObj.addGameObject(block, layer);
            ScreenChunkManager.addBlock(block, layer);

        }
        else {
            gameObj.addGameObject(block, layer + 1);
            ScreenChunkManager.addBlock(block, layer + 1);

        }
    }
}
