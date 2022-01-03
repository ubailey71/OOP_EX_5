package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

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
    //TODO: maybe add seed
    private final Random random;

    public Tree(GameObjectCollection gameObjects, Function<Float, Float> groundHeightAt, int layer,
                int seed) {
        this.gameObjects = gameObjects;
        this.groundHeightAt = groundHeightAt;
        this.layer = layer;
        this.random = new Random(seed);
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
            if (shouldNotPlaceObj(TREE_TAG) || isPrevPlaced) {
                curX += blockSize;
                isPrevPlaced = false;
                continue;
            }
            int groundHeightInBlocks = (int) Math.floor(groundHeightAt.apply(curX) / blockSize);
            int treeHeight = random.nextInt(groundHeightInBlocks - TREE_MIN_HEIGHT) + TREE_MIN_HEIGHT;
            for (int blockIdx = 1; blockIdx < treeHeight; blockIdx++) {
                Vector2 topLeftCorner = new Vector2(curX, (groundHeightInBlocks - blockIdx) * blockSize);
                Renderable blockRender =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASIC_TREE_COLOR));
                Block block = new Block(topLeftCorner, blockRender);
                block.setTag(TREE_TAG);
                gameObjects.addGameObject(block, layer);
            }
            createLeaves(new Vector2(curX, (groundHeightInBlocks - treeHeight + 1) * blockSize), gameObjects);
            isPrevPlaced = true;
            curX += blockSize;
        }
        gameObjects.layers().shouldLayersCollide(Layer.BACKGROUND + 3, Layer.STATIC_OBJECTS, true);
    }

    private boolean shouldNotPlaceObj(String objTag) {
        if (objTag.equals(TREE_TAG)) {
            return random.nextInt(COIN_FLIP_BOUND) > TREE_COVER_PERCENTAGE;
        }
        return random.nextInt(COIN_FLIP_BOUND) > LEAF_COVER_PERCENTAGE;
    }

    private void createLeaves(Vector2 treeTopLeftCorner, GameObjectCollection gameObjects) {
        Vector2 leafGridTLC = treeTopLeftCorner.add(Vector2.ONES.mult(-TREE_LEAF_OFFSET_SIZE * Block.SIZE));

        for (int rowIdx = 0; rowIdx < TREE_LEAF_OFFSET_SIZE * 2; rowIdx++) {
            for (int colIdx = 0; colIdx < TREE_LEAF_OFFSET_SIZE * 2 + 1; colIdx++) {
                if (shouldNotPlaceObj(LEAF_TAG)) {
                    continue;
                }
                Vector2 leafTopLeftCorner = leafGridTLC.add(new Vector2(
                        (float) colIdx * Block.SIZE,
                        (float) rowIdx * Block.SIZE));
                Renderable leafRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASIC_LEAF_COLOR));

                Leaf leaf = new Leaf(leafTopLeftCorner, leafRenderable, random);
                leaf.createLeaf(gameObjects, LEAF_TAG, layer + 1);
            }
        }
    }
}
