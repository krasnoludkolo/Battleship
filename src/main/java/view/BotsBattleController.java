package view;

import game.BattleshipGame;
import game.Cell;
import game.GameStatus;
import game.observer.BoardObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class BotsBattleController extends Controller implements BoardObserver {

    @FXML
    GridPane playerA;
    @FXML
    GridPane playerB;
    private String playerAName;
    private String playerBName;

    void init(BattleshipGame battleshipGame, String a, String b) {
        this.battleshipGame = battleshipGame;
        this.playerAName = a;
        this.playerBName = b;
        super.init("dev");
        battleshipGame.addBoardObserver(playerBName, this);
    }

    private void setGrid(GridPane grid, Cell[][] cells) {
        if (grid.getChildren().size() > 0) {
            grid.getChildren().clear();
        }
        GridPane gridPane = new GridPane();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                Button button = new Button();
                Image image = getImageFor(cells[i][j]);
                button.setGraphic(new ImageView(image));
                button.setStyle("-fx-background-color: transparent");
                gridPane.add(button, i, j);
            }
        }
        grid.getChildren().add(gridPane);
    }

    @Override
    public void update(GameStatus gameStatus) {
        Platform.runLater(() -> {
            GameStatus gameStatusForA = battleshipGame.getGameStatusFor(playerAName);
            setGrid(playerB, gameStatusForA.getEnemyBoardCells());
            GameStatus gameStatusForB = battleshipGame.getGameStatusFor(playerBName);
            setGrid(playerA, gameStatusForB.getEnemyBoardCells());
        });
    }
}
