package game.api;

import game.observer.BoardObserver;

import java.util.Optional;

public interface BattleshipGame {

    MoveResult makeMove(String playerName, Coordinates coordinates);

    GameStatus getGameStatusFor(String playerName);

    String getActivePlayer();

    void addBoardObserver(String name, BoardObserver observer);

    Optional<String> getWinner();

}
