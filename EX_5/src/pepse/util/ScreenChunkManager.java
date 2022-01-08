package pepse.util;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.world.Terrain;
import pepse.world.trees.Tree;

import java.util.ArrayDeque;
import java.util.Deque;


/**
 * make sure that there are always chunks of ground surrounding the player.
 */
public class ScreenChunkManager {

    private final int BLOCK_SIZE;
    private int worldMinX;
    private int worldMaxX;
    private final Tree tree;
    private final Terrain terrain;
    private final GameObjectCollection gameObjects;
    private final Deque<ScreenChunk> chunkList = new ArrayDeque<>();

    /**
     * Constructor.
     *
     * @param gameObjects .
     * @param windowDimensions .
     * @param tree tree object.
     * @param terrain terrain object.
     * @param avatarX the first avatar location.
     */
    public ScreenChunkManager(GameObjectCollection gameObjects, Vector2 windowDimensions,
                              Tree tree, Terrain terrain, float avatarX) {
        this.gameObjects = gameObjects;
        this.BLOCK_SIZE = (int) (windowDimensions.x() / 2);
        this.worldMinX = (int) (avatarX - BLOCK_SIZE / 2);
        this.worldMaxX = worldMinX + BLOCK_SIZE;
        this.tree = tree;
        this.terrain = terrain;
        addInitialChunks();
    }

    /*
    adds the first chunks.
     */
    private void addInitialChunks() {
        chunkList.addFirst(new ScreenChunk(worldMinX, worldMinX + BLOCK_SIZE,
                this.terrain, this.tree));
        addChunkLeft();
        addChunkRight();
    }

    /*
    adds chunk to right side.
     */
    private void addChunkRight() {
        chunkList.addLast(new ScreenChunk(worldMaxX, (int) Math.ceil(worldMaxX + BLOCK_SIZE),
                this.terrain, this.tree));
        worldMaxX = (int) Math.ceil(worldMaxX + BLOCK_SIZE);
    }

    /*
    removes chunk from right side.
     */
    private void removeChunkRight() {
        chunkList.removeLast().removeBlocks(gameObjects);
        worldMaxX = (int) Math.ceil(worldMaxX - BLOCK_SIZE);
    }

    /*
    adds chunk to left side.
     */
    private void addChunkLeft() {
        chunkList.addFirst(new ScreenChunk((int) Math.floor(worldMinX - BLOCK_SIZE), worldMinX,
                terrain, tree));
        worldMinX = (int) Math.floor(worldMinX - BLOCK_SIZE);
    }

    /*
    removes chunk from left side.
     */
    private void removeChunkLeft() {
        chunkList.removeFirst().removeBlocks(gameObjects);
        worldMinX = (int) Math.floor(worldMinX + BLOCK_SIZE);
    }


    /**
     * given the avatar current position, it removes unnecessary chunks and adds the new chunks.
     *
     * @param playerAXPosition first player position.
     * @param playerBXPosition second player position.
     */
    public void updateChunks(float playerAXPosition, float playerBXPosition) {

        if (Math.min(playerAXPosition, playerBXPosition) - BLOCK_SIZE < worldMinX) {
            addChunkLeft();
        }

        if (Math.min(playerAXPosition, playerBXPosition) > worldMinX + (BLOCK_SIZE * 2)) {
            removeChunkLeft();
        }

        if (Math.max(playerAXPosition, playerBXPosition) + BLOCK_SIZE > worldMaxX) {
            addChunkRight();
        }

        if (Math.max(playerAXPosition, playerBXPosition) < worldMaxX - (BLOCK_SIZE * 2)) {
            removeChunkRight();
        }
    }
}