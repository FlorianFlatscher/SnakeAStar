package sample.snake.engine;

import javafx.concurrent.Task;

public abstract class GameTask<GameType, ResultType> extends Task<ResultType> {
    protected GameType game;

    public GameTask(GameType game) {
        this.game = game;
    }
}