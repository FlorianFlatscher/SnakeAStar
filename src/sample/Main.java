package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.snake.engine.GameCanvas;
import sample.snake.engine.GameEngine;
import sample.snake.game.SnakeGame;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Window
        VBox root = new VBox();
        primaryStage.setTitle("Loading...");
        primaryStage.setScene(new Scene(root, 600, 300));
        primaryStage.setResizable(false);

        //Canvas
        GameCanvas canvas = new GameCanvas(600, 300);

        GameEngine gameEngine = new GameEngine();
        gameEngine.framesPassedProperty().addListener((observableValue, oldValue, newValue) -> {
            if ((long) newValue % 10 == 1) {
                primaryStage.setTitle("Snake (" + Math.round(gameEngine.getFps()) + " fps)");
            }
        });
        gameEngine.startGame(new SnakeGame(canvas, 20, 10));
        primaryStage.show();
        root.getChildren().addAll(canvas);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
