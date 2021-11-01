
public class Ship extends Entity {
    private mainLoop mainLoop;

    public Ship(mainLoop mainLoop, String ref, int x, int y) {
        super(ref,x,y);

        this.mainLoop = mainLoop;
    }

    public void move(long delta) {
        if ((dx < 0) && (x < 10)) {
            return;
        }
        if ((dx > 0) && (x > 750)) {
            return;
        }

        super.move(delta);
    }


    public void collidedWith(Entity other) {
        if (other instanceof Alien) {
            mainLoop.death();
        }
    }
}
/////