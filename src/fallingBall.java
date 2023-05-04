import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class fallingBall extends Application {
    private final double GRAVITY = 9.81; // m/s^2
    private final double MASS = 0.1; // kg
    private final double RADIUS = 10; // px
    private final double HEIGHT = 400; // px
    private double positionY = 0; // px
    private double velocityY = 0; // px/s
    private double potentialEnergy = MASS * GRAVITY * HEIGHT; // J


    public void start(Stage primaryStage) throws Exception {
        Circle ball = new Circle(RADIUS, Color.BLUE);
        ball.relocate(50, HEIGHT);
        Pane root = new Pane(ball);
        Scene scene = new Scene(root, 400, 500, Color.WHITE);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
            double deltaTime = 0.016; // s
            double forceY = MASS * GRAVITY; // N
            double accelerationY = forceY / MASS; // m/s^2
            velocityY += accelerationY * deltaTime; // px/s
            positionY += velocityY * deltaTime; // px
            ball.relocate(50, HEIGHT - positionY);

            double kineticEnergy = 0.5 * MASS * velocityY * velocityY; // J
            double totalEnergy = potentialEnergy + kineticEnergy; // J
            System.out.println("Potential energy: " + potentialEnergy + " J");
            System.out.println("Kinetic energy: " + kineticEnergy + " J");
            System.out.println("Total energy: " + totalEnergy + " J");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}