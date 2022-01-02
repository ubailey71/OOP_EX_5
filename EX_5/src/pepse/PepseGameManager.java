package pepse;

import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;

public class PepseGameManager extends GameManager {

    private static float CYCLE_LENGTH = 10;

    //TODO: remove constructor when done.
    public PepseGameManager(String title, Vector2 windowDimensions){
        super(title, windowDimensions);
    }

    @Override public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                                         UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);


        Sky.create(this.gameObjects(),windowController.getWindowDimensions(), Layer.BACKGROUND);
        Night.create(this.gameObjects(),Layer.BACKGROUND,windowController.getWindowDimensions(),CYCLE_LENGTH);
        Sun.create(this.gameObjects(),Layer.BACKGROUND+1,windowController.getWindowDimensions(),CYCLE_LENGTH);

        //TODO: make seed permanent.
        //terrain part
        int seed = 3;
        Vector2 windowDimensions = windowController.getWindowDimensions();
        Terrain terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS, windowDimensions, seed);
        terrain.createInRange(0, (int) windowDimensions.x());
    }

    public static void main(String[] args) {
        new PepseGameManager("Pepse", new Vector2(1200, 700)).run();
    }
}
