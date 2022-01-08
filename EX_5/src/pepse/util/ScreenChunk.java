package pepse.util;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import pepse.PepseGameManager;
import pepse.world.Terrain;
import pepse.world.trees.Tree;

import java.util.*;

/**
 * represents a single chunk of ground and holds all the object that are located between its borders.
 */
public class ScreenChunk {
    private final List<GameObject> groundInChunk = new ArrayList<>();
    private final List<GameObject> treesInChunk = new ArrayList<>();

    /**
     * Constructor
     *
     * @param minX left border
     * @param maxX right border
     */
    public ScreenChunk(int minX, int maxX, Terrain terrain, Tree tree) {
        terrain.createTerrainInRange(minX, maxX, groundInChunk);
        tree.createTreesInRange(minX, maxX, treesInChunk);
    }
    /**
     * removes all the game object that are located in this chunk.
     *
     * @param gameObjects .
     */
    public void removeBlocks(GameObjectCollection gameObjects) {

        for (GameObject block : groundInChunk) {
            if (!gameObjects.removeGameObject(block, Layer.STATIC_OBJECTS + 1))
                gameObjects.removeGameObject(block, Layer.STATIC_OBJECTS);
        }

        for (GameObject block : treesInChunk) {
            if (!gameObjects.removeGameObject(block, PepseGameManager.TREES_LAYER))
                if (!gameObjects.removeGameObject(block, PepseGameManager.TREES_LAYER + 1))
                    gameObjects.removeGameObject(block, PepseGameManager.TREES_LAYER + 2);
        }
    }
}