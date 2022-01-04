package pepse.world.avatarProperties;

import danogl.gui.rendering.Renderable;

public class AvatarAnimations {

    public Renderable stand;
    public Renderable walk;
    public Renderable acceleration_up;
    public Renderable acceleration_down;
    public Renderable fly;


    public AvatarAnimations (Renderable stand, Renderable walk, Renderable acceleration_up, Renderable fly,
                             Renderable acceleration_down){
        this.stand = stand;
        this.walk = walk;
        this.acceleration_up = acceleration_up;
        this.acceleration_down = acceleration_down;
        this.fly = fly;
    }
}
