package pepse.util;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class ScreenChunkManager {
    private static int worldMinX;
    private static int worldMaxX;
    private static GameObjectCollection gameObjects;
    private static Vector2 windowDimensions;
    private static BiConsumer<Integer, Integer> createTerrainInRange;
    private static BiConsumer<Integer, Integer> createTreeInRange;
    private static final ArrayList<ScreenChunk> chunkList = new ArrayList<>();

    public ScreenChunkManager(GameObjectCollection gameObjects, Vector2 windowDimensions,
                              BiConsumer<Integer, Integer> createTerrainInRange,
                              BiConsumer<Integer, Integer> createTreeInRange1) {
        ScreenChunkManager.gameObjects = gameObjects;
        ScreenChunkManager.windowDimensions = windowDimensions;
        ScreenChunkManager.createTerrainInRange = createTerrainInRange;
        ScreenChunkManager.createTreeInRange = createTreeInRange1;
        ScreenChunkManager.worldMinX = 0;
        ScreenChunkManager.worldMaxX = (int) windowDimensions.x();
        ScreenChunk firstChunk = new ScreenChunk(worldMinX, worldMaxX);
        chunkList.add(firstChunk);
    }

    public static void updateChunks(float avatarXPosition) {
        if (avatarXPosition - 2 * windowDimensions.x() > worldMinX){
            chunkList.get(0).removeBlocks(gameObjects);
            chunkList.remove(0);
            worldMinX += windowDimensions.x();
            System.out.println("min: " + worldMinX + ", max: " + worldMaxX + ", numOfBlocks: " + chunkList.size());
        }

        if (avatarXPosition + 2 * windowDimensions.x() < worldMaxX){
            chunkList.get(chunkList.size() - 1).removeBlocks(gameObjects);
            chunkList.remove(chunkList.size() - 1);
            worldMaxX -= windowDimensions.x();
            System.out.println("min: " + worldMinX + ", max: " + worldMaxX + ", numOfBlocks: " + chunkList.size());
        }
        if (avatarXPosition - windowDimensions.x() < worldMinX) {
            int newMinX = (int) (worldMinX - windowDimensions.x());
            chunkList.add(0, new ScreenChunk(newMinX, worldMinX));
            createTerrainInRange.accept(newMinX, worldMinX);
            createTreeInRange.accept(newMinX, worldMinX);
            worldMinX -= windowDimensions.x();
            System.out.println("min: " + worldMinX + ", max: " + worldMaxX + ", numOfBlocks: " + chunkList.size());

        }
        if (avatarXPosition + windowDimensions.x() > worldMaxX) {
            int newMaxX = (int) (worldMaxX + windowDimensions.x());
            chunkList.add(chunkList.size(), new ScreenChunk(worldMaxX, newMaxX));
            createTerrainInRange.accept(worldMaxX, newMaxX);
            createTreeInRange.accept(worldMaxX, newMaxX);
            worldMaxX += windowDimensions.x();
            System.out.println("min: " + worldMinX + ", max: " + worldMaxX + ", numOfBlocks: " + chunkList.size());
        }
    }

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
