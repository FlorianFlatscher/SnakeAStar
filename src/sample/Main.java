package sample;

import javafx.application.Application;
import javafx.scene.Scene;
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
        primaryStage.setScene(new Scene(root, 512, 512));
        primaryStage.setResizable(false);

        //Canvas
        GameCanvas canvas = new GameCanvas();
        GameEngine gameEngine = new GameEngine();
        gameEngine.framesPassedProperty().addListener((observableValue, oldValue, newValue) -> {
            if ((long) newValue % 10 == 1) {
                primaryStage.setTitle("Snake (" + Math.round(gameEngine.getFps()) + " fps)");
            }
        });

        root.getChildren().add(canvas);
        primaryStage.show();
        gameEngine.startGame(new SnakeGame(canvas));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
