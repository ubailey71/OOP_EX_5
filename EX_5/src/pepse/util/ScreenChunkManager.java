package pepse.util;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.function.BiConsumer;


/**
 * make sure that there are always chunks of ground surrounding the player.
 */
public class ScreenChunkManager {
    private static int worldMinX;
    private static int worldMaxX;
    private static GameObjectCollection gameObjects;
    private static Vector2 windowDimensions;
    private static BiConsumer<Integer, Integer> createTerrainInRange;
    private static BiConsumer<Integer, Integer> createTreeInRange;
    private static final ArrayList<ScreenChunk> chunkList = new ArrayList<>();


    /**
     * initializes the first chunk.
     * @param gameObjects .
     * @param windowDimensions .
     * @param createTerrainInRange .
     * @param createTreeInRange .
     */
    public static void create(GameObjectCollection gameObjects, Vector2 windowDimensions,
                              BiConsumer<Integer, Integer> createTerrainInRange,
                              BiConsumer<Integer, Integer> createTreeInRange) {
        ScreenChunkManager.gameObjects = gameObjects;
        ScreenChunkManager.windowDimensions = windowDimensions;
        ScreenChunkManager.createTerrainInRange = createTerrainInRange;
        ScreenChunkManager.createTreeInRange = createTreeInRange;
        ScreenChunkManager.worldMinX = 0;
        ScreenChunkManager.worldMaxX = (int) windowDimensions.x();
        ScreenChunk firstChunk = new ScreenChunk(worldMinX, worldMaxX);
        chunkList.add(firstChunk);
    }


    /**
     * given the avatar current position, it removes unnecessary chunks and adds the new chunks.
     * @param avatarXPosition the avatar x position.
     */
    public static void updateChunks(float avatarXPosition) {
        if (avatarXPosition - 2 * windowDimensions.x() > worldMinX){
            chunkList.get(0).removeBlocks(gameObjects);
            chunkList.remove(0);
            worldMinX += windowDimensions.x();
            return;
        }

        if (avatarXPosition + 2 * windowDimensions.x() < worldMaxX){
            chunkList.get(chunkList.size() - 1).removeBlocks(gameObjects);
            chunkList.remove(chunkList.size() - 1);
            worldMaxX -= windowDimensions.x();
            return;
        }
        if (avatarXPosition - windowDimensions.x() < worldMinX) {
            int newMinX = (int) (worldMinX - windowDimensions.x());
            chunkList.add(0, new ScreenChunk(newMinX, worldMinX));
            createTerrainInRange.accept(newMinX, worldMinX);
            createTreeInRange.accept(newMinX, worldMinX);
            worldMinX -= windowDimensions.x();
            return;
        }
        if (avatarXPosition + windowDimensions.x() > worldMaxX) {
            int newMaxX = (int) (worldMaxX + windowDimensions.x());
            chunkList.add(chunkList.size(), new ScreenChunk(worldMaxX, newMaxX));
            createTerrainInRange.accept(worldMaxX, newMaxX);
            createTreeInRange.accept(worldMaxX, newMaxX);
            worldMaxX += windowDimensions.x();
        }
    }

    /**
     * given a block and its layer, it adds it to the chunk who should own it.
     * @param block .
     * @param layer .
     */
    public static void addBlock(Block block, int layer) {
        int blockPos = (int) block.getTopLeftCorner().x();
        for (int chunkIdx = 0; chunkIdx < chunkList.size(); chunkIdx++) {
            ScreenChunk chunk = chunkList.get(chunkIdx);
            if (chunk.getChunkStart() <= blockPos && blockPos <= chunk.getChunkEnd()) {
                chunk.addBlock(block, layer);
            }
        }
    }
}