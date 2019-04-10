import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Star extends JPanel implements ActionListener {
    // Масштабування відбувається відносно центру координат,

// тому малювати фігуру бажано також симетрично центру
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
    // Для анімації руху
    private double dx = 1;
    private double tx = 0;
    private double dy = 1;
    private double ty = 6;
    private static int maxWidth;
    private static int maxHeight;

    public Star() {

// Таймер генеруватиме подію що 10 мс
        timer = new Timer(10, this);
        timer.start();
    }

    public void paint(Graphics g) {

        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
// Встановлюємо кольори
        g2d.setBackground(Color.black);
        g2d.setColor(Color.YELLOW);
        g2d.clearRect(0, 0, maxWidth+1, maxHeight+1);
// Встановлюємо параметри рендерингу
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
// Зсовуємо центр координат в центр вікна
        g2d.translate(maxWidth/2, maxHeight/2);
// Перетворення для анімації руху.
        g2d.translate(tx, ty);
// Створення зірки
        GeneralPath star = new GeneralPath();
        star.moveTo(points[0][0], points[0][1]);
        for (int k = 1; k < points.length; k++)
            star.lineTo(points[k][0], points[k][1]);
        star.closePath();
// Перетворення для анімації повороту. Якщо не задати 2 та 3 параметри
// поворот відбудеться відносно центру координат
        g2d.rotate(angle, star.getBounds2D().getCenterX(),
                star.getBounds2D().getCenterY());
//        g2d.rotate(angle);
// Перетворення для анімації масштабу
        g2d.scale(scale, 0.5);
// Перетворення для анімації зміни прозорості
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
//                (float)scale));
// Далі йдуть всі ті методи, що необхідні для власне малювання малюнку
        g2d.fill(star);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Приклад анімації");
        frame.add(new Star());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
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
//        if ( tx < -maxWidth/3 ) {
//            dx = -dx;
//        } else if ( tx > maxWidth/3 ) {
//            dx = -dx;
//        }
//        if ( ty < -maxHeight/3 ) {
//            dy = -dy;
//        } else if ( ty > maxHeight/3 ) {
//            dy = -dy;
//        }
        scale += delta;
//        angle += 0.01;
//        tx += dx;
//        ty += dy;

        repaint();

    }
}