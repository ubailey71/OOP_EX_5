package pepse;

import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Sky;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;

public class PepseGameManager extends GameManager {

    private static float CYCLE_LENGTH = 100;

    @Override public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                                         UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Sky.create(this.gameObjects(),windowController.getWindowDimensions(), Layer.BACKGROUND);
        Night.create(this.gameObjects(),Layer.BACKGROUND,windowController.getWindowDimensions(),CYCLE_LENGTH);
        Sun.create(this.gameObjects(),Layer.BACKGROUND+1,windowController.getWindowDimensions(),CYCLE_LENGTH);
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
