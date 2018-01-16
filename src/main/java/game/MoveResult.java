package game;

import lombok.Getter;

@Getter
public class MoveResult {

    private final GameStatus gameStatus;
    private final ActionResult actionResult;
    private final boolean nextTurn;

    private MoveResult(GameStatus gameStatus, ActionResult actionResult, boolean nextTurn) {
        this.gameStatus = gameStatus;
        this.actionResult = actionResult;
        this.nextTurn = nextTurn;
    }

    public static MoveResult hitMove(GameStatus gameStatus) {
        return new MoveResult(gameStatus, ActionResult.HIT, false);
    }

    public static MoveResult noPlayerInGame(GameStatus gameStatus) {
        return new MoveResult(gameStatus, ActionResult.NOT_PLAYER_IN_GAME, false);
    }

    public static MoveResult wrongTurn(GameStatus gameStatus) {
        return new MoveResult(gameStatus, ActionResult.NOT_PLAYER_TURN, false);
    }

    public static MoveResult missMove(GameStatus gameStatus) {
        return new MoveResult(gameStatus, ActionResult.MISS, true);
    }

    public static MoveResult wrongMove(GameStatus gameStatus, ActionResult actionResult) {
        return new MoveResult(gameStatus, actionResult, false);
    }

    public static MoveResult winningMove(GameStatus gameStatus) {
        return new MoveResult(gameStatus, ActionResult.WINNING_MOVE, false);
    }

    public static MoveResult sunkMove(GameStatus gameStatus) {
        return new MoveResult(gameStatus, ActionResult.SUNK, false);
    }

}
