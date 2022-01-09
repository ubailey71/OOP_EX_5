package pepse.world.avatarProperties;

import danogl.gui.rendering.Renderable;

/**
 * a utility class containing all must-have avatar animations
 */
public class AvatarAnimations {
    // standing animation
    public Renderable stand;
    // walking animation
    public Renderable walk;
    // upwards-acceleration animation
    public Renderable acceleration_up;
    // downwards-acceleration animation
    public Renderable acceleration_down;
    // flying animation
    public Renderable fly;

    /**
     * constructor
     * @param stand standing animation
     * @param walk walking animation
     * @param acceleration_up upwards-acceleration animation
     * @param fly flying animation
     * @param acceleration_down downwards-acceleration animation
     */
    public AvatarAnimations(Renderable stand, Renderable walk, Renderable acceleration_up, Renderable fly,
                            Renderable acceleration_down) {
        this.stand = stand;
        this.walk = walk;
        this.acceleration_up = acceleration_up;
        this.acceleration_down = acceleration_down;
        this.fly = fly;
    }
}
