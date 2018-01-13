package game;

import game.observer.BoardObserver;

public interface BattleshipGame {

    MoveResult makeMove(String playerName, Coordinates coordinates);
    GameStatus getGameStatusFor(String playerName);
    String getActivePlayer();
    void addBoardObserver(String name, BoardObserver observer);

}
