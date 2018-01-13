package view;

import game.*;
import game.observer.BoardObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class GameController implements BoardObserver {

    private BattleshipGame battleshipGame;
    private String playerName;

    @FXML
    GridPane playerBoard;
    @FXML
    GridPane enemyBoard;

    public void init(BattleshipGame battleshipGameObject, String playerName) {
        battleshipGame = battleshipGameObject;
        this.playerName = playerName;
        GameStatus gameStatus = battleshipGame.getGameStatusFor(playerName);
        initPlayerBoard(gameStatus.getPlayerBoardCells());
        initEnemyBoard(gameStatus.getEnemyBoardCells());
        battleshipGame.addBoardObserver(playerName, this);
    }

    private void initPlayerBoard(Cell[][] playerBoardCells) {
        if (playerBoard.getChildren().size() > 0) {
            playerBoard.getChildren().clear();
        }
        GridPane gridPane = new GridPane();
        for (int i = 0; i < playerBoardCells.length; i++) {
            for (int j = 0; j < playerBoardCells[0].length; j++) {
                Button button = new Button();
                String text = getTextFor(playerBoardCells[i][j]);
                button.setText(text);
                gridPane.add(button, i, j);
            }
        }
        playerBoard.getChildren().add(gridPane);
    }

    private void initEnemyBoard(Cell[][] enemyBoardCells) {
        if (enemyBoard.getChildren().size() > 0) {
            enemyBoard.getChildren().clear();
        }
        GridPane gridPane = new GridPane();
        for (int i = 0; i < enemyBoardCells.length; i++) {
            for (int j = 0; j < enemyBoardCells[0].length; j++) {
                Button button = new Button();
                final int x = i;
                final int y = j;
                String text = getTextFor(enemyBoardCells[x][y]);
                button.setText(text);
                button.setOnAction(e -> handleButtonClick(x, y));
                gridPane.add(button, x, y);
            }
        }
        enemyBoard.getChildren().add(gridPane);
    }

    private String getTextFor(Cell cell) {
        switch (cell) {
            case MISS:
                return "o";
            case HIT:
                return "X";
            case EMPTY:
                return " ";
            case SUNKEN_SHIP:
                return "$";
            case SHIP:
            case END_SHIP_UP:
            case END_SHIP_DOWN:
            case END_SHIP_RIGHT:
            case END_SHIP_LEFT:
            case ONE_CELL_SHIP:
                return "S";
        }
        return " ";
    }

    private void handleButtonClick(int x, int y) {
        MoveResult moveResult = battleshipGame.makeMove(playerName, new Coordinates(x, y));
        if (moveResult.isNextTurn()) {
            update(moveResult);
        }
    }

    private void update(MoveResult moveResult) {
        initEnemyBoard(moveResult.getGameStatus().getEnemyBoardCells());
        initPlayerBoard(moveResult.getGameStatus().getPlayerBoardCells());
    }

    @Override
    public void update(GameStatus gameStatus) {
        Platform.runLater(() -> {
            initPlayerBoard(gameStatus.getPlayerBoardCells());
            initEnemyBoard(gameStatus.getEnemyBoardCells());
        });
    }
}
