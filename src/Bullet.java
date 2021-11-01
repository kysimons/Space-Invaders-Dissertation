
public class Bullet extends Entity {
    private double moveSpeed = -300;
    private mainLoop mainLoop;
    private boolean used = false;

    public Bullet(mainLoop mainLoop, String sprite, int x, int y) {
        super(sprite,x,y);

        this.mainLoop = mainLoop;

        dy = moveSpeed;
    }

    public void move(long delta) {
        super.move(delta);

        if (y < -100) {
            mainLoop.remove(this);
        }
    }

    public void collidedWith(Entity other) {
        if (used) {
            return;
        }

        if (other instanceof Alien) {
            mainLoop.remove(this);
            mainLoop.remove(other);

            mainLoop.alienKilled();
            used = true;
        }

        if (other instanceof Object){
            mainLoop.remove(this);
            mainLoop.remove(other);
            mainLoop.killed();
            used = true;
        }
    }
}
/////