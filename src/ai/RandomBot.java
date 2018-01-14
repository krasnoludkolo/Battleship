package ai;

import game.BattleshipGame;
import game.Coordinates;

public class RandomBot extends AbstractBot {


    public RandomBot(BattleshipGame battleshipGame, String name) {
        super(battleshipGame, name, 100);
    }

    @Override
    protected Coordinates nextMoveCoordinates() {
        return getRandomCoordinates();
    }


}
