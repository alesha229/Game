package things;

import java.io.*;
import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

//класс-квадрат
final class Square {
    public int x, y, w, h;

    Square() {
    }

    Square(int _x, int _y, int _w, int _h) {
        x = _x;
        y = _y;
        w = _w;
        h = _h;
    }

    //пересечение квадратов
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
};

//для примера
final class Form extends JFrame {
    private javax.swing.Timer timer;
    private BufferedImage dimg = null;
    private Graphics hdc = null;
    private Square[] arr = null;
    private Square square = null;

    Form() {
        super();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Пример работы пересечений");
        setSize(640, 480);
        Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((ssize.width - getWidth()) / 2,
                (ssize.height - getHeight()) / 2);

        dimg = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        hdc = dimg.getGraphics();

        square = new Square();
        square.w = 50;
        square.h = 100;
        square.x = (getWidth() - square.w) / 2;
        square.y = getHeight() - square.h * 2;

        arr = new Square[3];
        arr[0] = new Square(90, 250, 100, 30);
        arr[1] = new Square(430, 150, 90, 100);
        arr[2] = new Square(250, 120, 50, 30);

        ActionListener tevent = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Form.this.repaint();
            }
        };
        timer = new javax.swing.Timer(10, tevent);
        timer.start();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                Form.this.on_keyPress(e);
            }
        });
    }

    @Override
    public void paint(Graphics dc) {
        hdc.setColor(Color.WHITE);
        hdc.fillRect(0, 0, getWidth(), getHeight());

        hdc.setColor(Color.BLUE);
        hdc.fillRect(square.x, square.y, square.w, square.h);

        hdc.setColor(Color.RED);
        for (Square q : arr) {
            hdc.fillRect(q.x, q.y, q.w, q.h);
        }
        dc.drawImage(dimg, 0, 0, null);
    }

    //управление пользователем синим квадратом
    void on_keyPress(KeyEvent e) {
        int vel = 3;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                square.x -= vel;
                break;
            case KeyEvent.VK_RIGHT:
                square.x += vel;
                break;
            case KeyEvent.VK_UP:
                square.y -= vel;
                break;
            case KeyEvent.VK_DOWN:
                square.y += vel;
                break;
        }

        //коллизия с пнями
        for (Square s : arr) {
            Square.isLockIntersect(square, s);
        }
    }
}


class BattleSquare {
    public static void main(String[] args) {
        Form form = new Form();
        form.setVisible(true);
    }
}