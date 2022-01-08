package pepse.util;

import danogl.collisions.GameObjectCollection;
import pepse.world.Block;

import java.util.*;

public class ScreenChunk {
    private final int  minX;
    private final int  maxX;
    private final Map<Block, Integer> blocksInChunk = new HashMap<>();

    public ScreenChunk(int minX, int maxX){
        this.minX = minX;
        this.maxX = maxX;
    }

    public void addBlock(Block block, int layer){
        blocksInChunk.put(block, layer) ;
    }

    public void removeBlocks(GameObjectCollection gameObjects){
        for (Block block: blocksInChunk.keySet()){
            gameObjects.removeGameObject(block, blocksInChunk.get(block));
//            System.out.println("removed: " + block.getTag());
        }
    }

    public int getChunkStart(){
        return minX;
    }

    public int getChunkEnd(){
        return maxX;
    }



}
