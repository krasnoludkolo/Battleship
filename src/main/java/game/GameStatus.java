package game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GameStatus {

    private final Cell[][] playerBoardCells;
    private final Cell[][] enemyBoardCells;

}
