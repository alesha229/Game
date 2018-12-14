package things;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;
    static Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int vert = sSize.height;
    static int hor = sSize.width;
    private boolean running;
    public static int WIDTH = vert;
    public static int HEIGHT = hor;
    public static String NAME = "GAME OF YEAR";
    private static float xx = 0f;
    private static float yy = 0f;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean sprint = false;
    float speed = 0f;
    Image img = (new ImageIcon(Game.class.getResource("bcg.png"))).getImage();
    Image tree = (new ImageIcon(Game.class.getResource("tree.png"))).getImage();
    public static Sprite heroUP;
    public static Sprite heroDown;
    public static Sprite sprt;
    public static Sprite heroRight;
    public static Sprite heroleft;
    private static int x1;
    private static int y1;
    ImageIcon ii = new ImageIcon(getClass().getResource("gg.gif"));

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void run() {
        long lastTime = System.currentTimeMillis();
        long delta;

        init();

        while (running) {

            delta = System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
            render();
            update(delta);
        }
    }


    public int x, y, w, h;

    Game() {
    }

    Game(int _x, int _y, int _w, int _h) {
        x = _x;
        y = _y;
        w = _w;
        h = _h;
    }

    public void init() {
        addKeyListener(new KeyInputHandler());
        heroUP = getSprite("things/man-up.png");
        heroDown = getSprite("things/man-down.png");
        heroleft = getSprite("things/man-left.png");
        heroRight = getSprite("things/man-right.png");
        sprt = getSprite("things/gg.png");


    }
    public static boolean isIntersect(Square a, Square b) {
        return ((a.x < (b.x + b.w)) &&
                (b.x < (a.x + a.w)) &&
                (a.y < (b.y + b.h)) &&
                (b.y < (a.y + a.h)));
    }

    //пересечение квадратов с выталкиванием
    public static boolean isLockIntersect(Square a, Square b) {
        if (!isIntersect(a, b))
            return false;
        int x0 = b.x - (a.x - b.w);
        int y0 = b.y - (a.y - b.h);
        int x1 = (a.x + a.w) - b.x;
        int y1 = (a.y + a.h) - b.y;
        if (x1 < x0)
            x0 = -x1;
        if (y1 < y0)
            y0 = -y1;

        if (Math.abs(x0) < Math.abs(y0))
            a.x += x0;
        else if (Math.abs(x0) > Math.abs(y0))
            a.y += y0;
        else {
            a.x += x0;
            a.y += y0;
        }
        return true;
    }



    public void painter(Graphics g)
    {
        x1 =  (int) xx;
        y1 = (int)yy;

        g.drawImage(img, x1, y1,null);

    }

    public void tree(Graphics g)
    {
        square = new Square();
        square.w = 50;
        square.h = 100;
        square.x    = (getWidth() - square.w) / 2;
        square.y    = getHeight() - square.h * 2;
        arr    = new Square[1];
        arr[0] = new Square(90, 250, 100,30);
        float h = tree.getHeight(null);
        float w = tree.getWidth(null);
        h = h/2;
        w = w/2;
        int widtht = 10;
        int heightt = 20;
        int posx=40;
        int posy=70;
        g.drawImage(tree, x1+posx*10-(int)w, y1+posy*10-(int)h,null);
    }
    private Graphics      hdc    = null;
    private Square[]      arr    = null;
    private Square        square = null;
    public void render() {

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            requestFocus();
            return;
        }


        Graphics g = bs.getDrawGraphics(); //ïîëó÷àåì Graphics èç ñîçäàííîé íàìè BufferStrategy
        g.setColor(Color.black); //âûáðàòü öâåò
        g.fillRect(0, 0, getWidth(), getHeight()); //çàïîëíèòü ïðÿìîóãîëüíèê
        painter(g);



        if(downPressed==true){
            heroDown.draw(g, hor/2-80, vert/2-60);
        }
        else if(rightPressed==true) {
            heroRight.draw(g, hor/2-80, vert/2-60);
        }
        else if(leftPressed==true) {
            heroleft.draw(g, hor/2-80, vert/2-60);
        }
        else if(upPressed==true) {
            heroUP.draw(g, hor/2-80, vert/2-60);
        }
        else {
            heroDown.draw(g, hor/2-80, vert/2-60);
        }
        tree(g);
        sprt.map(g,0,0);

        g.dispose();
        bs.show(); //ïîêàçàò
    }

    public void update(long delta) {

        if(sprint==true){
            speed=0.3f;
        }
        else {
            speed=0;
        }

        if (leftPressed == true) {
            xx = xx + 0.5f;
            xx = xx +speed;
        }
        if (rightPressed == true) {
            xx-=0.5f;
            xx=xx-speed;
        }
        if (upPressed == true) {
            yy= yy +0.5f;
            yy=yy+speed;
        }
        if (downPressed == true) {
            yy= yy - 0.5f;
            yy=yy-speed;
        }
    }

    public Sprite getSprite(String path) {
        BufferedImage sourceImage = null;

        try {
            URL url = this.getClass().getClassLoader().getResource(path);
            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sprite sprite = new Sprite(Toolkit.getDefaultToolkit().createImage(sourceImage.getSource()));

        return sprite;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setPreferredSize(new Dimension(hor, vert));
        JFrame frame = new JFrame(Game.NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(false);
        game.start();

    }

    private class KeyInputHandler extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                leftPressed = true;
                System.out.println("left");
                System.out.println(xx);
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                rightPressed = true;
                System.out.println("right");
                System.out.println(xx);
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                upPressed = true;
                System.out.println("up");
                System.out.println(yy);
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                downPressed = true;
                System.out.println("down");
                System.out.println(yy);
            }

            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                sprint = true;

            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                upPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                downPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                sprint = false;

            }

        }
    }
}