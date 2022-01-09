package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.avatarProperties.AvatarAnimations;
import pepse.world.avatarProperties.AvatarKeymap;

import java.awt.event.KeyEvent;

/**
 * An avatar that can move around the world.
 */
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
    private AvatarKeymap keymap;

    private Avatar(Vector2 topLeftCorner,
                   Vector2 dimensions,
                   UserInputListener inputListener,
                   AvatarAnimations animations,
                   AvatarKeymap keymap
    ) {
        super(topLeftCorner, dimensions, animations.stand);
        this.inputListener = inputListener;
        this.animations = animations;
        this.energyLevel = INITIAL_ENERGY_LEVEL;
        this.keymap = keymap;
    }

    private static String[] framePaths(String basePath, String suffix, int start, int end) {
        String[] frames = new String[(1 + end) - start];
        for (int frameNum = start; frameNum <= end; frameNum++) {
            frames[frameNum - start] = basePath + frameNum + suffix;
        }
        return frames;
    }

    private static AnimationRenderable getAnimation(String basePath, String suffix, int start, int end,
                                                    ImageReader imageReader) {
        return new AnimationRenderable(
                framePaths(basePath, suffix, start, end),
                imageReader,
                true,
                WAIT_TIME);
    }

    /**
     * This function creates an avatar that can travel the world and is followed by the camera. The can
     * stand, walk, jump and fly, and never reaches the end of the world.
     *
     * @param gameObjects   - The collection of all participating game objects.
     * @param layer         - The number of the layer to which the created avatar should be added.
     * @param topLeftCorner - The location of the top-left corner of the created avatar.
     * @param inputListener - Used for reading input from the user.
     * @param imageReader   - Used for reading images from disk or from within a jar.
     * @return A newly created Gameobject representing the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer,
                                Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        String basePath = "EX_5/src/pepse/knight-frames\\knight-frame";
        String suffix = ".png";

        Renderable standAnimation = getAnimation(basePath, suffix, 1, 33, imageReader);
        Renderable walkAnimation = getAnimation(basePath, suffix, 34, 41, imageReader);
        Renderable acceleration_up = getAnimation(basePath, suffix, 42, 86, imageReader);
        Renderable fly = getAnimation(basePath, suffix, 87, 163, imageReader);
        Renderable acceleration_down = getAnimation(basePath, suffix, 164, 184, imageReader);

        AvatarAnimations animations =
                new AvatarAnimations(standAnimation, walkAnimation, acceleration_up, fly, acceleration_down);

        AvatarKeymap keymap = new AvatarKeymap(KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_SPACE,
                KeyEvent.VK_SHIFT);

        Avatar avatar = new Avatar(topLeftCorner, AVATAR_SCALE, inputListener, animations, keymap);
        avatar.transform().setAccelerationY(GRAVITY_SPEED);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * This function creates an avatar that can travel the world and is followed by the camera. The can
     * stand, walk, jump and fly, and never reaches the end of the world.
     *
     * @param gameObjects   - The collection of all participating game objects.
     * @param layer         - The number of the layer to which the created avatar should be added.
     * @param topLeftCorner - The location of the top-left corner of the created avatar.
     * @param inputListener - Used for reading input from the user.
     * @param imageReader   - Used for reading images from disk or from within a jar.
     * @param basePath      - basic path (up to the frame number) to all the frames for the avatar animations
     * @param suffix        - the animation frames suffix (i.e .png)
     * @param keymap        - a AvatarKeyMap holding the mapping to all avatar actions
     * @return A newly created Gameobject representing the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer,
                                Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader,
                                String basePath,
                                String suffix,
                                AvatarKeymap keymap) {
        Renderable standAnimation = getAnimation(basePath, suffix, 1, 33, imageReader);
        Renderable walkAnimation = getAnimation(basePath, suffix, 34, 41, imageReader);
        Renderable acceleration_up = getAnimation(basePath, suffix, 42, 86, imageReader);
        Renderable fly = getAnimation(basePath, suffix, 87, 163, imageReader);
        Renderable acceleration_down = getAnimation(basePath, suffix, 164, 184, imageReader);

        AvatarAnimations animations =
                new AvatarAnimations(standAnimation, walkAnimation, acceleration_up, fly, acceleration_down);

        Avatar avatar = new Avatar(topLeftCorner, AVATAR_SCALE, inputListener, animations, keymap);
        avatar.transform().setAccelerationY(GRAVITY_SPEED);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }


    private void handleStanding(boolean isOnGround) {
        if (!inputListener.isKeyPressed(KeyEvent.KEY_PRESSED)) {
            if (isOnGround)
                this.renderer().setRenderable(this.animations.stand);
            this.transform().setVelocityX(0);
        }
    }

    private void handleHorizontalMovement(boolean isOnGround) {
        if (inputListener.isKeyPressed(keymap.moveRight)) {
            if (isOnGround)
                this.renderer().setRenderable(this.animations.walk);
            this.transform().setVelocityX(WALK_SPEED);
            isLookingLeft = false;
        }

        if (inputListener.isKeyPressed(keymap.moveLeft)) {
            if (isOnGround)
                this.renderer().setRenderable(this.animations.walk);
            this.transform().setVelocityX(-WALK_SPEED);
            isLookingLeft = true;
        }
    }

    private void handleVerticalMovement(boolean isOnGround) {
        boolean isFlying = false;
        if (inputListener.isKeyPressed(keymap.jump)) {
            if (isOnGround) {
                this.transform().setVelocityY(VERTICAL_JUMP_SPEED);
                return;
            }
        }

        if (inputListener.isKeyPressed(keymap.jump) && inputListener.isKeyPressed(keymap.fly)) {
            isFlying = energyLevel > 0;
        }

        if (isFlying) {
            energyLevel -= ENERGY_STEP;
            transform().setVelocityY(VERTICAL_FLIGHT_SPEED);
        }
        if (this.transform().getVelocity().y() < 0) {
            if (isFlying)
                renderer().setRenderable(animations.fly);
            else
                renderer().setRenderable(animations.acceleration_up);
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
            this.energyLevel = Math.min(this.energyLevel + ENERGY_STEP, INITIAL_ENERGY_LEVEL);
        }
        handleStanding(isOnGround);
        handleHorizontalMovement(isOnGround);
        handleVerticalMovement(isOnGround);
        this.renderer().setIsFlippedHorizontally(isLookingLeft);


    }
}
