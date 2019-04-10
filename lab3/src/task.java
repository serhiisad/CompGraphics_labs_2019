
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class task extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Image image = new Image("./bird_1.bmp");
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        // Display image on screen
        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root, 1000, 1000);


        // Animation
        int cycleCount = 2;
        int time = 2000;

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(time), root);
        scaleTransition.setToX(-2f);
        scaleTransition.setToY(1f);
        scaleTransition.setCycleCount(cycleCount);
        scaleTransition.setAutoReverse(true);

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(time), root);
        translateTransition1.setFromX(10);
        translateTransition1.setToX(300);
        translateTransition1.setFromY(20);
        translateTransition1.setToY(250);
        translateTransition1.setCycleCount(cycleCount+4);
        translateTransition1.setAutoReverse(true);


        TranslateTransition translateTransition2 = new TranslateTransition(Duration.millis(time), root);
        translateTransition2.setFromX(10);
        translateTransition2.setToX(-150);
        translateTransition2.setFromY(20);
        translateTransition2.setToY(-250);
        translateTransition2.setCycleCount(cycleCount);
        translateTransition2.setAutoReverse(true);
        
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(time), root);
        rotateTransition.setByAngle(-270f);
        rotateTransition.setCycleCount(cycleCount + 2);
        rotateTransition.setAutoReverse(true);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                translateTransition1, translateTransition2,
                scaleTransition,
                rotateTransition
        );

        parallelTransition.setCycleCount(Timeline.INDEFINITE);
        parallelTransition.play();

        primaryStage.setTitle("Angry bird");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
