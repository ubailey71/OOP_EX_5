package pepse.util;

import danogl.collisions.GameObjectCollection;
import pepse.world.Block;

import java.util.*;

/**
 * represents a single chunk of ground and holds all the object that are located between its borders.
 */
public class ScreenChunk {
    private final int  minX;
    private final int  maxX;
    private final Map<Block, Integer> blocksInChunk = new HashMap<>();

    /**
     * Constructor
     * @param minX left border
     * @param maxX right border
     */
    public ScreenChunk(int minX, int maxX){
        this.minX = minX;
        this.maxX = maxX;
    }

    /**
     * given a block and layer, it adds them to the blocks map who stores them.
     * @param block Block object
     * @param layer the layer of the block
     */
    public void addBlock(Block block, int layer){
        blocksInChunk.put(block, layer) ;
    }

    /**
     * removes all the game object that are located in this chunk.
     * @param gameObjects .
     */
    public void removeBlocks(GameObjectCollection gameObjects){
        for (Block block: blocksInChunk.keySet()){
            gameObjects.removeGameObject(block, blocksInChunk.get(block));
        }
    }

    /**
     * @return the right border value
     */
    public int getChunkStart(){
        return minX;
    }

    /**
     * @return the left border value
     */
    public int getChunkEnd(){
        return maxX;
    }



}