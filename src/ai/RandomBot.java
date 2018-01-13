package ai;

import game.BattleshipGame;
import game.Cell;
import game.GameStatus;
import game.Coordinates;
import game.observer.BoardObserver;

import java.util.Objects;
import java.util.Random;

public class RandomBot implements Runnable, BoardObserver {

    private BattleshipGame battleshipGame;
    private String name;
    private Random random = new Random();

    public RandomBot(BattleshipGame battleshipGame, String name) {
        this.battleshipGame = battleshipGame;
        this.name = name;
    }


    @Override
    public void run() {
        while (true){
            move();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void move() {
        String activePlayer = battleshipGame.getActivePlayer();
        if(Objects.equals(activePlayer,name)){
            int x;
            int y;
            GameStatus gameStatusFor = battleshipGame.getGameStatusFor(name);
            do {
                int maxX = gameStatusFor.getEnemyBoardCells().length;
                int maxY = gameStatusFor.getEnemyBoardCells()[0].length;
                x = random.nextInt(maxX);
                y = random.nextInt(maxY);
            }while (isNotEmpty(x, y, gameStatusFor));
            battleshipGame.makeMove(name,new Coordinates(x,y));
        }
    }

    private boolean isNotEmpty(int x, int y, GameStatus gameStatusFor) {
        Cell cell = gameStatusFor.getEnemyBoardCells()[x][y];
        return cell != Cell.EMPTY;
    }

    @Override
    public void update(GameStatus gameStatus) {

    }
}
