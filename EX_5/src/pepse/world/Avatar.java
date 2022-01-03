package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject {

    private static final Vector2 AVATAR_SCALE = new Vector2(50, 100);
    private static final int WALK_SPEED = 300;
    private static final int VERTICAL_FLIGHT_SPEED = -100;
    private static final int VERTICAL_JUMP_SPEED = -300;
    private static final int INITIAL_ENERGY_LEVEL = 100;
    private static final float ENERGY_STEP = 0.5f;
    private static final int GRAVITY_SPEED = 500;
    private static final float WAIT_TIME = 0.1f;

    private float energyLevel;
    private boolean isLookingLeft = false;
    private AvatarAnimations animations;
    private UserInputListener inputListener;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public Avatar(Vector2 topLeftCorner,
                  Vector2 dimensions,
                  UserInputListener inputListener,
                  AvatarAnimations animations
    ) {
        super(topLeftCorner, dimensions, animations.stand);
        this.inputListener = inputListener;
        this.animations = animations;
        this.energyLevel = INITIAL_ENERGY_LEVEL;
    }

    private static String[] framePaths(int start, int end) {
        String basePath = "EX_5/src/pepse/knight-frames\\knight-frame";
        String suffix = ".png";
        String[] frames = new String[(1 + end) - start];
        for (int frameNum = start; frameNum <= end; frameNum++) {
            frames[frameNum - start] = basePath + frameNum + suffix;
        }
        return frames;
    }

    public static Avatar create(GameObjectCollection gameObjects,
                                int layer,
                                Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        Renderable standAnimation = new AnimationRenderable(
                framePaths(1, 33),
                imageReader,
                true,
                WAIT_TIME);
        Renderable walkAnimation = new AnimationRenderable(
                framePaths(34, 41),
                imageReader,
                true,
                WAIT_TIME);
        Renderable acceleration_up = new AnimationRenderable(
                framePaths(42, 86),
                imageReader,
                true,
                WAIT_TIME);
        Renderable fly = new AnimationRenderable(
                framePaths(87, 163),
                imageReader,
                true,
                WAIT_TIME);
        Renderable acceleration_down = new AnimationRenderable(
                framePaths(164, 184),
                imageReader,
                true,
                WAIT_TIME);
        AvatarAnimations animations =
                new AvatarAnimations(standAnimation, walkAnimation, acceleration_up,fly, acceleration_down);

        Avatar avatar = new Avatar(topLeftCorner, AVATAR_SCALE, inputListener, animations);
        avatar.transform().setAccelerationY(GRAVITY_SPEED);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    private void handleStanding(boolean isOnGround){
        if (!inputListener.isKeyPressed(KeyEvent.KEY_PRESSED)) {
            if (isOnGround)
                this.renderer().setRenderable(this.animations.stand);
            this.transform().setVelocityX(0);
        }
    }
    private void handleHorizontalMovement(boolean isOnGround){
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (isOnGround)
                this.renderer().setRenderable(this.animations.walk);
            this.transform().setVelocityX(WALK_SPEED);
            isLookingLeft = false;
        }

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (isOnGround)
                this.renderer().setRenderable(this.animations.walk);
            this.transform().setVelocityX(-WALK_SPEED);
            isLookingLeft = true;
        }
    }
    private void handleVerticalMovement(boolean isOnGround){
        boolean isFlying = false;
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            if (isOnGround) {
                this.transform().setVelocityY(VERTICAL_JUMP_SPEED);
                return;
            }
        }

        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)){
            isFlying = energyLevel > 0;
        }

        if (isFlying){
            energyLevel -= ENERGY_STEP;
            transform().setVelocityY(VERTICAL_FLIGHT_SPEED);
        }
        if (this.transform().getVelocity().y() < 0) {
            if (isFlying) renderer().setRenderable(animations.fly);
            else renderer().setRenderable(animations.acceleration_up);
            return;
        }

        if (this.transform().getVelocity().y() > 0) {
            renderer().setRenderable(animations.acceleration_down);
        }
    }

    @Override public void update(float deltaTime) {
        super.update(deltaTime);
        boolean isOnGround = this.transform().getVelocity().y() == 0;
        if (isOnGround) {
            this.energyLevel = Math.min(this.energyLevel + ENERGY_STEP,INITIAL_ENERGY_LEVEL);
        }
        handleStanding(isOnGround);
        handleHorizontalMovement(isOnGround);
        handleVerticalMovement(isOnGround);
        this.renderer().setIsFlippedHorizontally(isLookingLeft);
    }
}
