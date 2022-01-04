package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.ScreenChunkManager;
import pepse.world.Block;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {
    private static final Color BASIC_TREE_COLOR = new Color(100, 50, 20);
    private static final Color BASIC_LEAF_COLOR = new Color(50, 200, 30);

    private static final int TREE_COVER_PERCENTAGE = 10;
    private static final int LEAF_COVER_PERCENTAGE = 70;
    private static final int TREE_LEAF_OFFSET_SIZE = 4;
    private static final int TREE_MIN_HEIGHT = 7;

    private static final int COIN_FLIP_BOUND = 100;

    private static final String TREE_TAG = "tree";
    private static final String LEAF_TAG = "leaf";


    private final Function<Float, Float> groundHeightAt;
    private final GameObjectCollection gameObjects;
    private final int layer;
    private int seed;

    /**
     * CONSTRUCTOR
     *
     * @param gameObjects    - The collection of all participating game objects.
     * @param groundHeightAt - reference to the method which checks the ground height.
     * @param layer          - number of the layer for the trees.
     * @param seed           - seed for the random object.
     */
    public Tree(GameObjectCollection gameObjects, Function<Float, Float> groundHeightAt, int layer,
                int seed) {
        this.groundHeightAt = groundHeightAt;
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.seed = seed;
    }

    /**
     * This method creates trees in a given range of x-values.
     *
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        float blockSize = Block.SIZE;
        float curX = (float) (Math.ceil(Math.abs(minX) / blockSize) * (int) Math.signum(minX) * blockSize);
        boolean isPrevPlaced = false;
        while (curX <= maxX) {
            Random random = new Random(Objects.hash(curX, seed));

            // make sure that tree coverage percentage is correct and no side by side trees
            if (shouldNotPlaceObj(TREE_TAG, random) || isPrevPlaced) {
                curX += blockSize;
                isPrevPlaced = false;
                continue;
            }
            int groundHeightInBlocks = (int) (groundHeightAt.apply(curX) / blockSize);
//            int groundHeightInBlocks = (int) Math.floor(groundHeightAt.apply(curX) / blockSize);
            createSingleTree(gameObjects, layer, groundHeightInBlocks, curX, random);
            isPrevPlaced = true;
            curX += blockSize;
        }

        gameObjects.layers().shouldLayersCollide(layer + 1, Layer.DEFAULT, true);
        gameObjects.layers().shouldLayersCollide(layer + 2, Layer.STATIC_OBJECTS, true);
    }


    /*
    create a single tree.
     */
    private static void createSingleTree(GameObjectCollection gameObjects, int layer,
                                         int groundHeightInBlocks, float curX, Random random) {
        if (groundHeightInBlocks - TREE_MIN_HEIGHT <= 0) {
            return;
        }
        int treeHeight = random.nextInt(groundHeightInBlocks - TREE_MIN_HEIGHT) + TREE_MIN_HEIGHT;
        float blockSize = Block.SIZE;

        for (int blockIdx = 1; blockIdx < treeHeight; blockIdx++) {
            Vector2 topLeftCorner = new Vector2(curX, (groundHeightInBlocks - blockIdx) * blockSize);
            Renderable blockRender =
                    new RectangleRenderable(ColorSupplier.approximateColor(BASIC_TREE_COLOR));
            Block block = new Block(topLeftCorner, blockRender);
            block.setTag(TREE_TAG);
            if (blockIdx == treeHeight - 1){
                gameObjects.addGameObject(block, layer + 1);
                ScreenChunkManager.addBlock(block, layer + 1);
            }
            else{
                gameObjects.addGameObject(block, layer);
                ScreenChunkManager.addBlock(block, layer);
            }
        }

        Vector2 treeTopLeftCorner = new Vector2(curX, (groundHeightInBlocks - treeHeight + 1) * blockSize);
        createLeaves(treeTopLeftCorner, gameObjects, random, layer + 2);
    }

    /*
    flips an unfair coin to determine if a tree/leaf should be placed.
     */
    private static boolean shouldNotPlaceObj(String objTag, Random random) {
        if (objTag.equals(TREE_TAG)) {
            return random.nextInt(COIN_FLIP_BOUND) > TREE_COVER_PERCENTAGE;
        }
        return random.nextInt(COIN_FLIP_BOUND) > LEAF_COVER_PERCENTAGE;
    }

    /*
    creates all the leaves for a single tree.
     */
    private static void createLeaves(Vector2 treeTopLeftCorner, GameObjectCollection gameObjects,
                                     Random random, int layer) {
        Vector2 leafGridTLC = treeTopLeftCorner.add(Vector2.ONES.mult(-TREE_LEAF_OFFSET_SIZE * Block.SIZE));

        for (int rowIdx = 0; rowIdx < TREE_LEAF_OFFSET_SIZE * 2; rowIdx++) {
            for (int colIdx = 0; colIdx < TREE_LEAF_OFFSET_SIZE * 2 + 1; colIdx++) {
                if (shouldNotPlaceObj(LEAF_TAG, random)) {
                    continue;
                }
                Vector2 leafTopLeftCorner = leafGridTLC.add(new Vector2(
                        (float) colIdx * Block.SIZE,
                        (float) rowIdx * Block.SIZE));
                Renderable leafRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASIC_LEAF_COLOR));

                Leaf leaf = new Leaf(leafTopLeftCorner, leafRenderable, random);
                leaf.createLeaf(gameObjects, LEAF_TAG, layer);
            }
        }
    }
}
