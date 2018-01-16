package view;

import game.api.*;
import game.observer.BoardObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class GameController extends Controller implements BoardObserver {

    @FXML
    GridPane playerBoard;
    @FXML
    GridPane enemyBoard;

    public void init(BattleshipGame battleshipGameObject, String playerName) {
        super.init("dev");
        battleshipGame = battleshipGameObject;
        this.playerName = playerName;
        GameStatus gameStatus = battleshipGame.getGameStatusFor(playerName);
        initPlayerBoard(gameStatus.getPlayerBoardCells());
        initEnemyBoard(gameStatus.getEnemyBoardCells());
        battleshipGame.addBoardObserver(playerName, this);
    }


    private void initPlayerBoard(Cell[][] playerBoardCells) {
        playerBoard.setGridLinesVisible(true);
        if (playerBoard.getChildren().size() > 0) {
            playerBoard.getChildren().clear();
        }
        GridPane gridPane = new GridPane();
        for (int i = 0; i < playerBoardCells.length; i++) {
            for (int j = 0; j < playerBoardCells[0].length; j++) {
                Button button = new Button();
                Image image = getImageFor(playerBoardCells[i][j]);
                button.setGraphic(new ImageView(image));
                button.setStyle("-fx-background-color: transparent");
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
                Image image = getImageFor(enemyBoardCells[i][j]);
                button.setGraphic(new ImageView(image));
                button.setOnAction(e -> handleButtonClick(x, y));
                button.setStyle("-fx-background-color: transparent");
                gridPane.add(button, x, y);
            }
        }
        enemyBoard.getChildren().add(gridPane);
    }

    private void handleButtonClick(int x, int y) {
        MoveResult moveResult = battleshipGame.makeMove(playerName, new Coordinates(x, y));
        update(moveResult);
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
