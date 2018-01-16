package game.logic;

import game.*;
import game.observer.BoardObserver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

public class RestBattleshipGame implements BattleshipGame {

    private Map<String, PlayerBoard> playersMap = new HashMap<>();
    private String activePlayer;
    private Map<String, BoardObserver> observers = new HashMap<>();
    private GameState gameState = GameState.RUNNING;

    public RestBattleshipGame(NewPlayer first, NewPlayer second, int size) {
        playersMap.put(first.getName(), new PlayerBoard(first.getShips(), size));
        playersMap.put(second.getName(), new PlayerBoard(second.getShips(), size));
        activePlayer = first.getName();
    }


    @Override
    public synchronized MoveResult makeMove(String playerName, Coordinates coordinates) {
        PlayerBoard playerBoard = getEnemyBoard(playerName);
        MovePossible movePossible = isMovePossible(playerName, coordinates, playerBoard);
        if (!movePossible.isPossibleMove()) {
            return movePossible.getResultIfWrongMove();
        }

        BoardMoveResult moveResult = playerBoard.hit(coordinates);
        updateAllObservers();
        if (moveResult.isHit() || moveResult.isSunk()) { //TODO moze ewentualnie rozdzielic
            boolean winningMove = checkIfGameEnded();
            if (winningMove) {
                gameState = GameState.END;
                return MoveResult.winningMove(getGameStatusFor(playerName));
            }
            if (moveResult.isSunk()) {
                return MoveResult.sunkMove(getGameStatusFor(playerName));
            }
            return MoveResult.hitMove(getGameStatusFor(playerName));
        }
        changeActivePlayer();
        return MoveResult.missMove(getGameStatusFor(playerName));
    }

    private boolean checkIfGameEnded() {
        return playersMap.values().stream().mapToInt(board -> board.hasShipsLeft() ? 0 : 1).sum() != 0;
    }

    private PlayerBoard getEnemyBoard(String playerName) {
        ArrayList<String> players = new ArrayList<>(playersMap.keySet());
        int i = players.indexOf(playerName);
        String enemy = players.get((i + 1) % players.size());
        return playersMap.get(enemy);
    }

    private MovePossible isMovePossible(String playerName, Coordinates coordinates, PlayerBoard playerBoard) {
        if (gameState == GameState.END) {
            return new MovePossible(false, MoveResult.wrongMove(getGameStatusFor(playerName), ActionResult.GAME_ENDED));
        }
        if (notPlayerInGame(playerName)) {
            return new MovePossible(false, MoveResult.noPlayerInGame(getGameStatusFor(playerName)));
        }
        if (notPlayerTurn(playerName)) {
            return new MovePossible(false, MoveResult.wrongTurn(getGameStatusFor(playerName)));
        }
        if (playerBoard.moveOutOfBoard(coordinates)) {
            return new MovePossible(false, MoveResult.wrongMove(getGameStatusFor(playerName), ActionResult.OUT_OF_BOARD));
        }
        if (playerBoard.isPlaceAlreadyToHit(coordinates)) {
            return new MovePossible(false, MoveResult.wrongMove(getGameStatusFor(playerName), ActionResult.HIT_HIT));
        }
        PlayerBoard board = getEnemyBoard(playerName);
        if (board.isPlaceAlreadyMiss(coordinates)) {
            return new MovePossible(false, MoveResult.wrongMove(getGameStatusFor(playerName), ActionResult.HIT_MISS));
        }
        return new MovePossible(true, null);
    }

    private boolean notPlayerInGame(String playerName) {
        return !playersMap.keySet().contains(playerName);
    }

    private boolean notPlayerTurn(String playerName) {
        return !Objects.equals(playerName, activePlayer);
    }

    private void changeActivePlayer() {
        ArrayList<String> players = new ArrayList<>(playersMap.keySet());
        int i = players.indexOf(activePlayer);
        activePlayer = players.get((i + 1) % players.size());
    }

    @Override
    public GameStatus getGameStatusFor(String playerName) {
        Cell[][] playerBoardCells = playersMap.get(playerName).getCellsArrayAsOwner();
        Cell[][] enemyBoardCells = getEnemyBoard(playerName).getCellsArrayAsEnemy();
        return new GameStatus(playerBoardCells, enemyBoardCells);
    }

    @Override
    public String getActivePlayer() {
        return activePlayer;
    }

    @Override
    public void addBoardObserver(String name, BoardObserver observer) {
        observers.put(name, observer);
    }

    @Override
    public Optional<String> getWinner() {
        if (gameState == GameState.RUNNING) {
            return Optional.empty();
        }
        return Optional.of(activePlayer);
    }

    private void updateAllObservers() {
        observers.forEach((key, value) -> value.update(getGameStatusFor(key)));
    }

    @AllArgsConstructor
    @Getter
    private class MovePossible {
        boolean possibleMove;
        MoveResult resultIfWrongMove;

    }
}
