package game;

import lombok.Data;

@Data
public class GameStatus {

    private final Cell[][] playerBoardCells;
    private final Cell[][] enemyBoardCells;

}
