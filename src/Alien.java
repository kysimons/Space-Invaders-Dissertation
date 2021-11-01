

public class Alien extends Entity {
    private double moveSpeed = 75;
    private mainLoop mainLoop;

    public Alien(mainLoop mainLoop, String ref, int x, int y) {
        super(ref,x,y);

        this.mainLoop = mainLoop;
        dx = -moveSpeed;
    }

    public void move(long delta) {
        if ((dx < 0) && (x < 10)) {
            mainLoop.update();
        }
        if ((dx > 0) && (x > 750)) {
            mainLoop.update();
        }

        super.move(delta);
    }

    public void doLogic() {
        dx = -dx;
        y += 10;

        if (y > 570) {
            mainLoop.death();
        }
    }

    public void collidedWith(Entity other) {
    }
}
/////