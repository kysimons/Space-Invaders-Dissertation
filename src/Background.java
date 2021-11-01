public class Background extends Entity {
    private mainLoop mainLoop;

    public Background(mainLoop mainLoop, String ref, int x, int y) {
        super(ref, x, y);

        this.mainLoop = mainLoop;
    }

    public void collidedWith(Entity other) {
    }
}
/////