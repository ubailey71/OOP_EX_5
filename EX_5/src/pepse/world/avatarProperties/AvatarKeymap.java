package pepse.world.avatarProperties;

/**
 * a utility class for mapping user input keys to avatar actions
 */
public class AvatarKeymap {
    // key to press to move right
    public int moveRight;
    // key to press to move left
    public int moveLeft;
    //  key to press to jump
    public int jump;
    // key to press to  fly
    public int fly;

    /**
     *
     * @param moveRight key to press to move right
     * @param moveLeft key to press to move left
     * @param jump key to press to jump
     * @param fly key to press to  fly
     */
    public AvatarKeymap(int moveRight, int moveLeft, int jump, int fly) {
        this.moveRight = moveRight;
        this.moveLeft = moveLeft;
        this.jump = jump;
        this.fly = fly;
    }
}
