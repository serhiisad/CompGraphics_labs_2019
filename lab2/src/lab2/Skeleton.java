package lab2;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.geom.AffineTransform;


public class Skeleton extends JPanel implements ActionListener{

    // Масштабування відбувається відносно центру координат,
// тому малювати фігуру бажано також симетрично центру

    Timer timer;

    // Для анімації руху
//    private double dx = 20;
    private double tx = 50;
//    private double dy = 20;
    private double ty = 50;

    private double angle = 0;
    private double radius = 50;

    // Для анімації масштабування
    private double scale = 1;
    private double delta = 0.01;

    private static int maxWidth;
    private static int maxHeight;

    public Skeleton() {
        timer = new Timer(80, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.orange);
        g2d.clearRect(0, 0, maxWidth, maxHeight);

        //rendering setting
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // picture from lab1
        g2d.setColor(Color.black);
        g2d.drawLine(300, 250, 200, 300);
        g2d.drawLine(300, 250, 400, 300);
        g2d.drawLine(450, 160, 500, 100);

        g2d.setColor(Color.cyan);
        double points[][] = {
                {150, 83}, {150, 250}, {450, 250}, {450, 160}, {200, 160}
        };
        GeneralPath polygon = new GeneralPath();
        polygon.moveTo(points[0][0], points[0][1]);
        for (int k = 1; k < points.length; k++)
            polygon.lineTo(points[k][0], points[k][1]);
        polygon.closePath();
        g2d.fill(polygon);

        g2d.setColor(Color.magenta);
        g2d.fillOval(160, 300, 50, 50);
        g2d.fillOval(390, 300, 50, 50);

        // drawing 1 primitive
        g2d.setColor(Color.black);
        g2d.drawLine(maxWidth/2, 0, maxWidth/2, maxHeight);

        //drawing frame (JOIN_BEVEL)
        BasicStroke bs = new BasicStroke(16, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(bs);
        g2d.setColor(Color.blue);
        g2d.drawRect(650, 35, 400, 400);

        //star inside the frame

        // Зсовуємо центр координат до точки біля лівого верхнього кута рамки
        g2d.translate(840, 180);

        //TODO
        // Перетворення для анімації руху.
        g2d.translate(tx, ty);

        // Створення зірки
//         points = new double [][] {
//                { 42, 38 }, { 52, 62 }, { 72, 68 }, { 52, 80 },
//                { 60, 105 }, { 40, 85 }, { 15, 102 }, { 28, 75 },
//                { 9, 58 }, { 32, 20 }, { 42, 38 }
//        };

        points = new double[][]  {
                { -100, -15 }, { -25, -25 }, { 0, -90 }, { 25, -25 },
                { 100, -15 }, { 50, 25 }, { 60, 100 }, { 0, 50 },
                { -60, 100 }, { -50, 25 }, { -100, -15 }
        };

        GradientPaint gp = new GradientPaint(5, 25,
                new Color(255,255,0), 20, 2, new Color(0,0,255), true);
        g2d.setPaint(gp);

        GeneralPath star = new GeneralPath();
        star.moveTo(points[0][0], points[0][1]);
        for (int k = 1; k < points.length; k++)
            star.lineTo(points[k][0], points[k][1]);

        star.closePath();

        // Перетворення для анімації масштабу
        g2d.scale(scale, 0.99);

        g2d.fill(star);
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Lab2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.add(new Skeleton());

        frame.setVisible(true);

        // for using real paint area dimensions
        Dimension size = frame.getSize();
        Insets insets = frame.getInsets();
        maxWidth = size.width - insets.left - insets.right - 1;
        maxHeight = size.height - insets.top - insets.bottom - 1;

    }


    // Цей метод буде викликано щоразу, як спрацює таймер
    public void actionPerformed(ActionEvent e) {

        if(scale > 1.5){
            delta =  -delta;
        }
        else if(scale < 0.5){
            delta+= 0.03;
        }

        tx = radius * Math.cos(angle);
        ty = radius * Math.sin(angle);
        angle += Math.toRadians(30);
        scale += delta;

        repaint();
    }
}
