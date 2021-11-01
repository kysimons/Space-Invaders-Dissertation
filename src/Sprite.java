import java.awt.Graphics;
import java.awt.Image;

public class Sprite {
    private java.awt.Image image;

    public Sprite(java.awt.Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    public void draw(Graphics g,int x,int y) {
        g.drawImage(image,x,y,null);
    }
}
/////