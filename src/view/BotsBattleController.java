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

public class BotsBattleController implements BoardObserver {

    private BattleshipGame battleshipGame;
    private String playerAName;
    private String playerBName;
    private Image hit;
    private Image empty;
    private Image miss;
    private Image ship;
    private Image sunk;

    @FXML
    GridPane playerA;
    @FXML
    GridPane playerB;

    void init(BattleshipGame battleshipGame, String a, String b) {
        this.battleshipGame = battleshipGame;
        this.playerAName = a;
        this.playerBName = b;
        initImages();
        battleshipGame.addBoardObserver(playerAName, this);
    }


    private void initImages() {
        hit = new Image(getClass().getResourceAsStream("resources/hit.png"));
        empty = new Image(getClass().getResourceAsStream("resources/empty.png"));
        miss = new Image(getClass().getResourceAsStream("resources/miss.png"));
        ship = new Image(getClass().getResourceAsStream("resources/ship.png"));
        sunk = new Image(getClass().getResourceAsStream("resources/sunk.png"));
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

    private Image getImageFor(Cell cell) {
        switch (cell) {
            case MISS:
                return miss;
            case HIT:
                return hit;
            case EMPTY:
                return empty;
            case SUNKEN_SHIP:
                return sunk;
            case SHIP:
            case END_SHIP_UP:
            case END_SHIP_DOWN:
            case END_SHIP_RIGHT:
            case END_SHIP_LEFT:
            case ONE_CELL_SHIP:
                return ship;
        }
        return empty;
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
