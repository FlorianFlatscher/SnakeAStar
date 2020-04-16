package sample.snake.game.sprites;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class Sprite {
    Image[] frames;
    long frameCount = 0;
    long nanosLastFrame;
    double fps;


    public Sprite(Image spriteSheet, double fps) {
        frames = new Image[(int) (spriteSheet.getWidth() / 32 * (spriteSheet.getHeight() / 32))];
        PixelReader reader = spriteSheet.getPixelReader();
        for (int x = 0; x < spriteSheet.getWidth(); x+= 32) {
            for (int y = 0; y < spriteSheet.getHeight(); y++) {
                WritableImage newImage = new WritableImage(reader, x, y, 32, 32);
                frames[(int) (x / 32 * (y / 32 * (spriteSheet.getWidth() /32)))] = (Image) newImage;
            }
        }
        this.fps = fps;
    }

    public void display(GraphicsContext gc, int x, int y, int width, int height) {
        if (System.nanoTime() - nanosLastFrame > 1_000_000_000/fps) {            nanosLastFrame = System.nanoTime();
            nanosLastFrame = System.nanoTime();
            frameCount++;
        }
        gc.drawImage(frames[(int) (frameCount % frames.length)], x, y, width, height);
    }
}
