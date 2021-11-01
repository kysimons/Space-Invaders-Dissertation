public class AlienBullet extends Entity {
    private double moveSpeed = -300;
    private mainLoop mainLoop;
    private boolean used = false;

    public AlienBullet(mainLoop mainLoop, String sprite, int x, int y) {
        super(sprite,x,y);

        this.mainLoop = mainLoop;

        dy = moveSpeed;
    }

    public void move(long delta) {
        super.rmove(delta);

        if (y < -100) {
            mainLoop.remove(this);
        }
    }

    public void collidedWith(Entity other) {
        if (used) {
            return;
        }
        if (other instanceof Ship){

            mainLoop.alive--;
            System.out.println(mainLoop.alive);
            mainLoop.remove(this);
            if (mainLoop.alive == 0) {
                mainLoop.death();
            }
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
