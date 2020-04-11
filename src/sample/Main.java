package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.snake.game.SnakeGame;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Window
        VBox root = new VBox();
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getScene().getStylesheets().add("sample/style/style.css");

        //Game
        Canvas gameCanvas = new Canvas(600, 600);
        SnakeGame game = new SnakeGame(new Dimension2D(10, 10));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!game.update(gameCanvas.getGraphicsContext2D())) {
                    this.stop();
                }
            }
        };
        primaryStage.getScene().setOnKeyPressed(game::keyPressed);
        timer.start();

        //GameSettings
        Slider snakeSpeed = new Slider(0.001, 1, 0.001);
        game.snakeSpeedProperty().bindBidirectional(snakeSpeed.valueProperty());
        HBox settings = new HBox(10, new VBox(4, snakeSpeed, new Label("Speed")));
        settings.setId("settings");
        root.getChildren().addAll(gameCanvas, settings);


        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
