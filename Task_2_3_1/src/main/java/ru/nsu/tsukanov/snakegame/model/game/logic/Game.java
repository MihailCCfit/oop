package ru.nsu.tsukanov.snakegame.model.game.logic;

import ru.nsu.tsukanov.snakegame.model.game.field.GameField;
import ru.nsu.tsukanov.snakegame.model.players.PlayerListener;
import ru.nsu.tsukanov.snakegame.model.units.Empty;
import ru.nsu.tsukanov.snakegame.model.units.GameUnit;
import ru.nsu.tsukanov.snakegame.model.units.Snake;
import ru.nsu.tsukanov.snakegame.model.units.SnakeBody;
import ru.nsu.tsukanov.snakegame.model.units.snake.Direction;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private final GameField field;
    private final Map<Integer, Snake> snakeMap;
    private final Map<Integer, PlayerListener> players = new HashMap<>();
    private final GameLogic gameLogic;
    private int tick = 0;

    private GameUnit fieldGet(int x, int y) {
        return field.get(x, y);
    }

    public Game(GameField field, Map<Integer, Snake> snakes) {
        this.field = field;
        this.snakeMap = snakes;
        snakes.values().forEach((snake -> {
            field.set(snake.getHead());
        }));
        gameLogic = new GameLogic(this);
    }

    public boolean tick() {
        if (snakeMap.values().stream().anyMatch((Snake::isControllable))) {
            players.forEach(((snakeId, playerListener) -> {
                Snake snake = snakeMap.get(snakeId);
                if (snake.isControllable()) {
                    Direction nextDirection = playerListener.nextDirection();
                    if (nextDirection != null) {
                        snake.changeDirection(nextDirection);

                    }
                }
            }
            ));
            if (tick % 2 == 0) {

                snakeMap.values().forEach(this::moveSnake);
            }

            if (tick % 5 == 0) {
                gameLogic.spawnFood();
            }
            tick++;
            if (tick >= 10) {
                tick = 0;
            }

            return true;
        } else {
            return false;
        }
    }

//    @Deprecated
    public void moveSnakes() {
        players.forEach(((snakeId, playerListener) -> {
            Snake snake = snakeMap.get(snakeId);
            if (snake.isControllable()) {
                Direction nextDirection = playerListener.nextDirection();
                if (nextDirection != null) {
                    snake.changeDirection(nextDirection);

                }
            }
        }));
        snakeMap.values().forEach(this::moveSnake);
    }

    public Map<Integer, Integer> getResults() {
        Map<Integer, Integer> results = new HashMap<>();
        snakeMap.forEach(((id, snake) -> {
            results.put(id, snake.length());
        }));
        return results;
    }

    public void addPlayer(Integer integer, PlayerListener player) {
        if (snakeMap.containsKey(integer)) {
            players.put(integer, player);
        }
    }

    private void moveSnake(Snake snake) {
        if (!snake.isControllable()) {
            return;
        }
        if (!snake.getBody().isEmpty()) {
            if (!snake.isGrowing()) {
                SnakeBody body = snake.getBody().getLast();
                field.set(new Empty(body.getX(), body.getY()));
            }
        } else {
            var head = snake.getHead();
            field.set(new Empty(head.getX(), head.getY()));

        }
        if (gameLogic.moveSnake(snake)) {
            setGameUnit(snake.getHead());
            if (!snake.getBody().isEmpty()) {
                setGameUnit(snake.getBody().getFirst());
            }
        }


    }

    public void setGameUnit(GameUnit unit) {
        field.set(unit);
    }

    public GameUnit getUnitAt(int x, int y) {
        return field.get(x, y);
    }

    public GameField getField() {
        return field;
    }

    public Map<Integer, Snake> getSnakeMap() {
        return snakeMap;
    }

    public int width() {
        return field.width();
    }

    public int height() {
        return field.height();
    }

    public Game getCopy() {
        GameField gameField = new GameField(field.width(), field.height());
        field.getAll().forEach((gameUnit) -> {
            gameField.set(gameUnit.getCopy());
        });
        Map<Integer, Snake> newSnakeMap = new HashMap<>();
        snakeMap.forEach((id, snake) -> {
            Snake newSnake = new Snake((SnakeBody) gameField.get(snake.getHead().getX(), snake.getHead().getY()));
            newSnakeMap.put(id, newSnake);
        });

        return new Game(gameField, newSnakeMap);
    }

    public void changeDirection(Integer snakeID, Direction direction) {
        snakeMap.get(snakeID).changeDirection(direction);
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }
}
