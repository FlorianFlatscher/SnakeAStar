package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.snake.SnakeEngine;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 512, 512));
        primaryStage.setResizable(false);

        //Canvas
        Canvas canvas = new Canvas( 512, 512 );
        root.getChildren().add( canvas );



        new SnakeEngine(canvas).start();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
