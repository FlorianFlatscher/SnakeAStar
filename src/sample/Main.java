package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.snake.engine.Game;
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

        GameEngine gameEngine = new GameEngine(canvas);

        gameEngine.startGame(new SnakeGame(20, 10));
        primaryStage.show();

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                primaryStage.setTitle("Snake (" + Math.round(gameEngine.getFps()) + " fps)");
            }
        }.start();



        root.getChildren().addAll(canvas);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
