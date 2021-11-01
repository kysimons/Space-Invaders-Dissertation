import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.sound.sampled.*;
import javax.swing.*;



public class mainLoop<AudioStream> extends Canvas {
    private String text = "";
    private boolean keyPress = true;
    private boolean left = false;
    private boolean right = false;
    private boolean fire = false;
    private boolean logic = false;
    private boolean showScore = true;
    private int score = 0;
    public int alive = 3;
    private Entity object;
    private Entity background;
    private double move = 300;
    private long last = 0;
    private long interval = 500;
    private int alienC;
    private int lives;
    private BufferStrategy strategy;
    private boolean gameRunning = true;
    private ArrayList entities = new ArrayList();
    private ArrayList removeList = new ArrayList();
    private Entity ship;

    public void update() {
        logic = true;
    }

    public void remove(Entity entity) {
        removeList.add(entity);
    }


    public void death() {
        text = "Oh no! They got you, try again?";
        keyPress = true;
        //score = 0;
        //alive = 3;
    }

    public void win() {
        text = "Well done! You Win!";
        keyPress = true;
    }

    public void killed(){
        for (int i = 0; i<entities.size(); i++){
            Entity entity = (Entity) entities.get(i);
        }
    }








    private void init() {
        background = new Background(this,"sprites/earth.jpg",-200,0);
        entities.add(background);

        ship = new Ship(this,"sprites/ship3.png",370,550);
        entities.add(ship);

        for(int line=0; line<5; line++){
            object = new Object(this,"sprites/asteroid2.jpg", 100+(line*140),450);
            entities.add(object);
        }


        alienC = 0;
        for (int row=0;row<4;row++) {
            for (int x=0;x<12;x++) {
                Entity alien = new Alien(this,"sprites/inavder3.jpg",100+(x*50),(50)+row*50);
                entities.add(alien);
                alienC++;
            }
        }
    }




    public void alienKilled() {
        alienC--; //change this so it never becomes a negetive number
        score+=20;

        String filepath ="invaderkilled.wav";

        musicStuff musicObject = new musicStuff();
        musicObject.playMusic(filepath); //dont have this created every time

        if (alienC == 0) {
            win();
        }

        for (int i=0;i<entities.size();i++) {
            Entity entity = (Entity) entities.get(i);

            if (entity instanceof Alien) {
                entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
            }
        }
    }

    public void fire() {
        if (System.currentTimeMillis() - last < interval) {
            return;
        }

        last = System.currentTimeMillis();
        Bullet shot = new Bullet(this,"sprites/shot.gif",ship.getX()+10,ship.getY()-30);
        AlienBullet shot2 = new AlienBullet(this,"sprites/shot.gif", ThreadLocalRandom.current().nextInt(1, 700 + 1), ship.getY()-350);
        entities.add(shot);
        entities.add(shot2);

        String filepath ="bip.wav";

        musicStuff musicObject = new musicStuff();
        musicObject.playMusic(filepath);
    }

    public mainLoop() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        JFrame container = new JFrame("Space Invaders by Kynan");

        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);

        setBounds(0,0,800,600);
        panel.add(this);

        setIgnoreRepaint(true);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addKeyListener(new KeyInputHandler());

        requestFocus();

        createBufferStrategy(2);
        strategy = getBufferStrategy();

        init();
    }

    private void start() {
        entities.clear();
        init();
        text = "";
        alive = 3;
        score = 0;

        left = false;
        right = false;
        fire = false;
    }

    public void loop() {
        long lastLoopTime = System.currentTimeMillis();

        while (gameRunning) {
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0,0,800,600);

            if (!keyPress) {
                for (int i=0;i<entities.size();i++) {
                    Entity entity = (Entity) entities.get(i);

                    entity.move(delta);
                }
            }

            for (int i=0;i<entities.size();i++) {
                Entity entity = (Entity) entities.get(i);

                entity.draw(g);
            }

            for (int p=0;p<entities.size();p++) {
                for (int s=p+1;s<entities.size();s++) {
                    Entity me = (Entity) entities.get(p);
                    Entity him = (Entity) entities.get(s);

                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    }
                }
            }

            entities.removeAll(removeList);
            removeList.clear();

            if (logic) {
                for (int i=0;i<entities.size();i++) {
                    Entity entity = (Entity) entities.get(i);
                    entity.doLogic();
                }

                logic = false;
            }

            if (showScore) {
                g.setColor(Color.white);
                g.drawString(text,(800-g.getFontMetrics().stringWidth(text))/2,250);
                g.drawString("Score: " + score,(80-g.getFontMetrics().stringWidth("Score: "))/2,30);
                g.drawString("Lives: " + alive, (1400-g.getFontMetrics().stringWidth("Lives: "))/2, 30);
            }

            g.dispose();
            strategy.show();

            ship.setHorizontalMovement(0);

            if ((left) && (!right)) {
                ship.setHorizontalMovement(-move);
            } else if ((right) && (!left)) {
                ship.setHorizontalMovement(move);
            }

            if (fire) {
                fire();
            }

            try { Thread.sleep(10); } catch (Exception e) {}
        }
    }

    private class KeyInputHandler extends KeyAdapter {
        private int pressCount = 1;

        public void keyPressed(KeyEvent e) {
            if (keyPress) {
                return;
            }


            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                left = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                right = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                fire = true;
            }

        }

        public void keyReleased(KeyEvent e) {
            if (keyPress) {
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                left = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                right = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                fire = false;
            }
        }


        public void keyTyped(KeyEvent e) {

            if (keyPress) {
                if (pressCount == 1) {
                    keyPress = false;
                    start();
                    pressCount = 0;
                } else {
                    pressCount++;
                }
            }

            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }

    public static void main(String argv[]) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        mainLoop g =new mainLoop();

        g.loop();
    }
}
/////