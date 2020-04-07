package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.snake.game.SnakeGame;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Window
        VBox root = new VBox();
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);

        //Game
        Canvas gameCanvas = new Canvas(1200, 600);
        SnakeGame game = new SnakeGame(new Dimension2D(600, 300));
        root.getChildren().add(gameCanvas);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                game.update(gameCanvas.getGraphicsContext2D());
            }
        };
        timer.start();
        primaryStage.getScene().setOnKeyPressed(game::keyPressed);

        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
