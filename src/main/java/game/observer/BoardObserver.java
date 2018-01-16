package game.observer;

import game.GameStatus;

public interface BoardObserver {

    void update(GameStatus gameStatus);
}
