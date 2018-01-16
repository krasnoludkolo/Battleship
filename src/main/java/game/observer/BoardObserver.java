package game.observer;

import game.api.GameStatus;

public interface BoardObserver {

    void update(GameStatus gameStatus);
}
