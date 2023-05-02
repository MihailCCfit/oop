package model.game.logic.events;

import model.game.logic.Game;
import model.units.Snake;

public abstract class SnakeEvent  implements Runnable{
    protected Snake snake;
    protected Game game;

    public SnakeEvent(Snake snake, Game game) {
        this.snake = snake;
        this.game = game;
    }
}