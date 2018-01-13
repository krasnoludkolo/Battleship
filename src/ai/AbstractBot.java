package ai;

import game.*;
import game.observer.BoardObserver;

abstract class AbstractBot implements Runnable, BoardObserver {

    private BattleshipGame battleshipGame;
    private String name;
    private long sleepTime;
    GameStatus lastGameStatus;

    AbstractBot(BattleshipGame battleshipGame, String name, long sleepTime) {
        this.battleshipGame = battleshipGame;
        this.name = name;
        this.sleepTime = sleepTime;
        lastGameStatus = battleshipGame.getGameStatusFor(name);
    }


    @Override
    public void run() {
        MoveResult move;
        do {
            Coordinates coordinates = nextMoveCoordinates();
            move = battleshipGame.makeMove(name, coordinates);

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        } while (move.getActionResult() != ActionResult.GAME_ENDED);
        System.out.println("koniec");

    }

    protected abstract Coordinates nextMoveCoordinates();


    @Override
    public void update(GameStatus gameStatus) {
        lastGameStatus = gameStatus;
    }

}
