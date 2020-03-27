package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.snake.engine.GameCanvas;

import java.awt.*;

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
        root.getChildren().add(canvas);


        primaryStage.show();
        canvas.framesPassedProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if ((long) newValue % 10 == 1) {
                    primaryStage.setTitle("Snake (" + Math.round(canvas.getFps()) + " fps)");
                }
            }
        });
        canvas.startEngine();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
