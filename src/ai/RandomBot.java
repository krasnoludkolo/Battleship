package ai;

import game.BattleshipGame;
import game.Cell;
import game.Coordinates;
import game.GameStatus;

import java.util.Random;

public class RandomBot extends AbstractBot {

    private Random random = new Random();

    public RandomBot(BattleshipGame battleshipGame, String name) {
        super(battleshipGame, name, 300);
    }

    @Override
    protected Coordinates nextMoveCoordinates() {
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

    private boolean isNotEmpty(int x, int y, GameStatus gameStatusFor) {
        Cell cell = gameStatusFor.getEnemyBoardCells()[x][y];
        return cell != Cell.EMPTY;
    }

}
