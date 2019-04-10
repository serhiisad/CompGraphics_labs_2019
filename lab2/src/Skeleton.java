import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Skeleton extends JPanel {

    private static int maxWidth;
    private static int maxHeight;

    // Всі дії, пов'язані з малюванням, виконуються в цьому методі
    public void paint(Graphics g) {
// Оскільки Java2D є надбудовою над старішою бібліотекою, необхідно робити це приведення
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        g2d.setBackground(Color.black);
        g2d.clearRect(0, 0, maxWidth, maxHeight);

        g2d.setColor(Color.yellow);
        g2d.drawLine(0, 50, maxWidth, maxHeight);

// Далі йде безпосередньо малювання. Для прикладу намалюємо такий рядок
        g2d.drawString("Привіт, Java 2D!", 50, 50);


        // primitives

//        GradientPaint gp = new GradientPaint(5, 25,
//                Color.YELLOW, 20, 2, Color.BLUE, true);
//        g2d.setPaint(gp);
//        g2d.fillRect(20, 20, 50, 50);
//        g2d.drawRect(120, 20, 90, 60);
//        g2d.fillRoundRect(250, 20, 70, 60, 25, 25);
//        g2d.draw(new Ellipse2D.Double(10, 100, 80, 100));
//        g2d.fillArc(120, 130, 110, 100, 5, 150);
//        g2d.drawOval(270, 130, 50, 50);



////..............................................................................

//        GradientPaint gp = new GradientPaint(5, 25,
//                new Color(255,255,0), 20, 2, new Color(0,0,255), true);
//        g2d.setPaint(gp);
//
//        double points[][] = {
//                { 0, 85 }, { 75, 75 }, { 100, 10 }, { 125, 75 },
//                { 200, 85 }, { 150, 125 }, { 160, 190 }, { 100, 150 },
//                { 40, 190 }, { 50, 125 }, { 0, 85 }
//        };
//        GeneralPath star = new GeneralPath();
//        g2d.translate(70, 12);
//        star.moveTo(points[0][0], points[0][1]);
//        for (int k = 1; k < points.length; k++)
//            star.lineTo(points[k][0], points[k][1]);
//        star.closePath();
//        g2d.fill(star);
////g2d.draw(star);


//        // Конструктор приймає три параметри: товщину лінії, тип декорації кінців
//        // та з'єднань
//        BasicStroke bs1 = new BasicStroke(16, BasicStroke.CAP_BUTT,
//                BasicStroke.JOIN_BEVEL);
//        g2d.setStroke(bs1);
//        g2d.drawLine(20, 30, 250, 30);
//        BasicStroke bs2 = new BasicStroke(16, BasicStroke.CAP_ROUND,
//                BasicStroke.JOIN_BEVEL);
//        g2d.setStroke(bs2);
//        g2d.drawLine(20, 80, 250, 80);
//        BasicStroke bs3 = new BasicStroke(16, BasicStroke.CAP_SQUARE,
//                BasicStroke.JOIN_BEVEL);
//        g2d.setStroke(bs3);
//        g2d.drawLine(20, 130, 250, 130);
//        // Скидаємо налаштування пензля на стандартні
//        BasicStroke bs4 = new BasicStroke();
//        g2d.setStroke(bs4);
//        g2d.setColor(new Color(150,150,255));
//        g2d.drawLine(20, 20, 20, 140);
//        g2d.drawLine(250, 20, 250, 140);
//        g2d.drawLine(258, 20, 258, 140);

//        BasicStroke bs1 = new BasicStroke(16, BasicStroke.CAP_ROUND,
//                BasicStroke.JOIN_BEVEL);
//        g2d.setStroke(bs1);
//        g2d.drawRect(15, 75, 80, 50);
//        BasicStroke bs2 = new BasicStroke(16, BasicStroke.CAP_ROUND,
//                BasicStroke.JOIN_MITER);
//        g2d.setStroke(bs2);
//        g2d.drawRect(125, 75, 80, 50);
//        BasicStroke bs3 = new BasicStroke(16, BasicStroke.CAP_ROUND,
//                BasicStroke.JOIN_ROUND);
//        g2d.setStroke(bs3);
//        g2d.drawRect(235, 75, 80, 50);
    }

    public static void main(String[] args) {
        // Створюємо нове графічне вікно (формочка). Параметр конструктора - заголовок вікна
                JFrame frame = new JFrame("Привіт, Java 2D!");
        // Визначаємо поведінку програми при закритті вікна (ЛКМ на "хрестик")
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Визначаємо розмір вікна
        frame.setSize(500, 500);
        // Якщо позиція прив'язана до null, вікно з'явиться в центрі екрану
        frame.setLocationRelativeTo(null);
        // Забороняємо змінювати розміри вікна
        frame.setResizable(false);
        // Додаємо до вікна панель, що і описується даним класом
        // Зауважте, що точка входу в програму - метод main, може бути й в іншому класі
        frame.add(new Skeleton());
        // Показуємо форму. Важливо додати всі компоненти на форму до того, як зробити її видимою.
        frame.setVisible(true);

        Dimension size = frame.getSize();
        Insets insets = frame.getInsets();
        maxWidth = size.width - insets.left - insets.right - 1;
        maxHeight = size.height - insets.top - insets.bottom - 1;

    }
}