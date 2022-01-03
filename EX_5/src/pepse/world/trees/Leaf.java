package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

public class Leaf extends Block {
    private static final float LEAF_WIDTH_WIDEN_FACTOR = 1.2f;
    private static final float LEAF_WIDTH_NARROW_FACTOR = 0.9f;
    private static final int LEAF_WIDTH_TRANSITION_TIME = 3;

    private static final float LEAF_INITIAL_ANGLE = -10f;
    private static final float LEAF_FINAL_ANGLE = 10f;
    private static final int ANGLE_TRANSITION_TIME = 2;

    private static final int FALLING_SPEED = 70;
    private static final int FADEOUT_TIME = 10;
    private static final int LIFE_CYCLE_LENGTH_BOUND = 250;
    private static final int DEATH_LENGTH_BOUND = 20;

    private static final float HORIZONTAL_MOVEMENT_INIT_VELOCITY = -30f;
    private static final float HORIZONTAL_MOVEMENT_FINAL_VELOCITY = 30f;
    private static final int HORIZONTAL_MOVEMENT_TRANSITION_TIME = 2;

    private Transition<Float> horizontalTransition;
    Transition<Vector2> widthChangeTransition;
    Transition<Float> angleTransition;

    private final Random random;
    private int deathTime;
    Vector2 originalLocation;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable, Random random) {
        super(topLeftCorner, renderable);
        this.random = random;
        originalLocation = topLeftCorner;
        physics().setMass(1);
        deathTime = 2;
    }


    public void createLeaf(GameObjectCollection gameObjects, String tag, int layer) {
        this.setTag(tag);
        gameObjects.addGameObject(this, layer);

        createRegularLeafMovement(this);
        horizontalTransition = createHorizontalTransition(this);
        this.removeComponent(horizontalTransition);

        ScheduledTask leafLifeCycle = new ScheduledTask(
                this,
                random.nextInt(LIFE_CYCLE_LENGTH_BOUND) + deathTime + FADEOUT_TIME,
                true,
                () -> {
                    this.transform().setVelocityY(FALLING_SPEED);
                    this.addComponent(horizontalTransition);
                    this.renderer().fadeOut(FADEOUT_TIME, this::reviveLeaf);
                });
    }

    private void createRegularLeafMovement(Leaf leaf) {
        float delayTime = (random.nextInt(200)) / (float) 100;
        new ScheduledTask(
                leaf,
                delayTime,
                false,
                () -> {
                    angleTransition = createAngleTransition(leaf);
                    widthChangeTransition = createWidthChangeTransition(leaf);
                });
    }

    private static Transition<Float> createAngleTransition(Leaf leaf) {
        return new Transition<>(
                leaf,
                leaf.renderer()::setRenderableAngle,
                LEAF_INITIAL_ANGLE,
                LEAF_FINAL_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                ANGLE_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    private static Transition<Vector2> createWidthChangeTransition(Leaf leaf) {
        Vector2 leafInitDim = leaf.getDimensions();

        return new Transition<>(
                leaf,
                leaf::setDimensions,
                leafInitDim.multX(LEAF_WIDTH_NARROW_FACTOR),
                leafInitDim.multX(LEAF_WIDTH_WIDEN_FACTOR),
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                LEAF_WIDTH_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    private static Transition<Float> createHorizontalTransition(Leaf leaf) {
        return new Transition<>(
                leaf,
                leaf.transform()::setVelocityX,
                HORIZONTAL_MOVEMENT_INIT_VELOCITY,
                HORIZONTAL_MOVEMENT_FINAL_VELOCITY,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                HORIZONTAL_MOVEMENT_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    private void reviveLeaf() {
        deathTime = random.nextInt(DEATH_LENGTH_BOUND);
        ScheduledTask revive = new ScheduledTask(
                this,
                deathTime,
                false,
                () -> {
                    this.setTopLeftCorner(originalLocation);
                    this.addComponent(angleTransition);
                    this.addComponent(widthChangeTransition);
                    this.renderer().fadeIn(1);
                }
        );
    }

        @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.removeComponent(horizontalTransition);
        this.removeComponent(angleTransition);
        this.removeComponent(widthChangeTransition);
        this.transform().setVelocityY(0);
        this.transform().setVelocityX(0);


    }
}
