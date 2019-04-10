
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.geom.Ellipse2D;

public class Task extends JPanel implements ActionListener{

    // moving figure
    double points[][] = {
            { -100, -15 }, { -25, -25 }, { 0, -90 }, { 25, -25 },
            { 100, -15 }, { 50, 25 }, { 60, 100 }, { 0, 50 },
            { -60, 100 }, { -50, 25 }, { -100, -15 }
    };
    Timer timer;

    // Для анімації повороту
    private double angle = 0;
    // Для анімації масштабування
    private double scale = 1;
    private double delta = 0.01;
//    // Для анімації руху
    private double dx = 1;
    private double tx = 0;
    private double dy = 1;
    private double ty = 6;
    private static int maxWidth;
    private static int maxHeight;

    public Task() {
        timer = new Timer(10, this);
        timer.start();
    }


    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        g2d.setBackground(Color.pink);
        g2d.clearRect(0, 0, maxWidth+1, maxHeight+1);

//        g2d.setColor(Color.yellow);
//        g2d.drawLine(0, 50, maxWidth, maxHeight);
    // picture from lab 1



    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Lab 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(new Task());
        frame.setVisible(true);

        Dimension size = frame.getSize();
        Insets insets = frame.getInsets();
        maxWidth = size.width - insets.left - insets.right - 1;
        maxHeight = size.height - insets.top - insets.bottom - 1;
    }


    // Цей метод буде викликано щоразу, як спрацює таймер
    public void actionPerformed(ActionEvent e) {
        if ( scale < 0.01 ) {
            delta = -delta;
        } else if (scale > 0.99) {
            delta = -delta;
        }
        if ( tx < -maxWidth/3 ) {
            dx = -dx;
        } else if ( tx > maxWidth/3 ) {
            dx = -dx;
        }
        if ( ty < -maxHeight/3 ) {
            dy = -dy;
        } else if ( ty > maxHeight/3 ) {
            dy = -dy;
        }
        scale += delta;
        angle += 0.01;
        tx += dx;
        ty += dy;

        repaint();

    }
}
