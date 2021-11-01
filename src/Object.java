public class Object extends Entity {
    private mainLoop mainLoop;

    public Object(mainLoop mainLoop, String ref, int x, int y) {
        super(ref, x, y);

        this.mainLoop = mainLoop;
    }

    public void collidedWith(Entity other) {
    }
}
/////