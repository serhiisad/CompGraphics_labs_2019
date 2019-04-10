import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class Picture extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 600, 500);
        scene.setFill(Color.ORANGE);

        //scene

        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                150.0, 83.0,
                150.0, 250.0,
                450.0, 250.0,
                450.0, 160.0,
                200.0, 160.0

            });
        root.getChildren().add(polygon);
        polygon.setFill(Color.AQUA);


        Line l1 = new Line(300.0f, 250.0f, 200.0f, 300.0f);
        root.getChildren().add(l1);
        l1.setStroke(Color.BLACK);

        Line l2 = new Line(300.0f, 250.0f, 400.0f, 300.0f);
        root.getChildren().add(l2);
        l2.setStroke(Color.BLACK);

        Line l3 = new Line(450.0f, 160.0f, 500.0f, 100.0f);
        root.getChildren().add(l3);
        l3.setStroke(Color.BLACK);

        Circle c1 = new Circle(180.0, 320.0, 35.0);
        root.getChildren().add(c1);
        c1.setFill(Color.VIOLET);

        Circle c2 = new Circle(420.0, 320.0, 35.0);
        root.getChildren().add(c2);
        c2.setFill(Color.VIOLET);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
