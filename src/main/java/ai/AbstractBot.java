package ai;

import game.*;
import game.observer.BoardObserver;

import java.util.Random;

abstract class AbstractBot implements Runnable, BoardObserver {


    private BattleshipGame battleshipGame;
    private String name;
    private long sleepTime;
    protected GameStatus lastGameStatus;
    protected MoveResult lastMoveResult;
    protected Random random = new Random();

    AbstractBot(BattleshipGame battleshipGame, String name, long sleepTime) {
        this.battleshipGame = battleshipGame;
        this.name = name;
        this.sleepTime = sleepTime;
        lastGameStatus = battleshipGame.getGameStatusFor(name);
    }


    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MoveResult move;
        do {
            Coordinates coordinates = nextMoveCoordinates();
            move = battleshipGame.makeMove(name, coordinates);
            if (move.getActionResult() != ActionResult.NOT_PLAYER_TURN) {
                lastMoveResult = move;

            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        } while (move.getActionResult() != ActionResult.GAME_ENDED);
        System.out.println("koniec");

    }

    protected abstract Coordinates nextMoveCoordinates();

    protected Coordinates getRandomCoordinates() {
        int x;
        int y;
        GameStatus gameStatusFor = lastGameStatus;
        do {
            int maxX = gameStatusFor.getEnemyBoardCells().length;
            int maxY = gameStatusFor.getEnemyBoardCells()[0].length;
            x = random.nextInt(maxX);
            y = random.nextInt(maxY);
        } while (isNotEmpty(x, y, gameStatusFor));
        return new Coordinates(x, y);
    }

    protected boolean isNotEmpty(int x, int y, GameStatus gameStatusFor) {
        Cell cell = gameStatusFor.getEnemyBoardCells()[x][y];
        return cell != Cell.EMPTY;
    }


    @Override
    public void update(GameStatus gameStatus) {
        lastGameStatus = gameStatus;
    }

}
