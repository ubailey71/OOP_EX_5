package pepse.util;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;

public class CameraManager extends GameObject {

    private static int EXTRA_SPACE = 200;

    private final Vector2 originalCamDimensions;
    private final Camera cam;
    private final Avatar playerA;
    private final Avatar playerB;
    private final UserInputListener inputListener;
    private final int changeTrackModeKey;
    private boolean trackBothPlayers = true;

    public CameraManager(
                         Camera cam,
                         Avatar playerA,
                         Avatar playerB,
                         UserInputListener inputListener,
                         int changeTrackModeKey) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.originalCamDimensions = cam.getDimensions().getImmutableCopy();
        this.cam = cam;
        this.playerA = playerA;
        this.playerB = playerB;
        this.inputListener = inputListener;
        this.changeTrackModeKey = changeTrackModeKey;
    }

    private Vector2 getCamCenter() {
        return playerA.transform().getCenter().add(playerB.transform().getCenter()).mult(0.5f);
    }

    private float getDistance(float a, float b){
        return Math.abs(a-b);
    }

    private Vector2 getCamSize() {

        float xDistance = getDistance(
                playerA.transform().getCenter().x(),
                playerB.transform().getCenter().x()
        );
        float yDistance = getDistance(
                playerA.transform().getCenter().y(),
                playerB.transform().getCenter().y()
        );
        float size = Math.max(xDistance,yDistance) + EXTRA_SPACE;
        return new Vector2(size,size);
    }

    private void trackBothPlayersUpdate(){
        cam.setToFollow(null,Vector2.ZERO);
        cam.transform().setCenter(getCamCenter());
        cam.setDimensions(getCamSize());
    }
    private void trackOnePlayerUpdate(){
        cam.setDimensions(this.originalCamDimensions);
        cam.setToFollow(playerA,Vector2.ZERO);
    }

    @Override public void update(float deltaTime) {
        super.update(deltaTime);
        if(inputListener.wasKeyReleasedThisFrame(changeTrackModeKey)) trackBothPlayers = !trackBothPlayers;
        if (trackBothPlayers) trackBothPlayersUpdate();
        else trackOnePlayerUpdate();
    }
}
