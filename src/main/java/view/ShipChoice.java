package view;

import game.api.Coordinates;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ShipChoice extends Controller {


    private final MainMenuController mainMenu;
    private GridPane board;
    private Label shipsLeft;
    private TextField playerName;
    private HBox ui;

    private Button[][] grid;
    private Stack<Integer> shipsSize;
    private List<List<Coordinates>> setShips = new ArrayList<>();
    private boolean validPosition = true;
    private Direction shipDirection = Direction.UP;
    private int size;
    private int activeShipSize;

    ShipChoice(String graphicName, Stack<Integer> shipsSize, int size, MainMenuController game) {
        super.init(graphicName);
        this.shipsSize = shipsSize;
        activeShipSize = shipsSize.pop();
        this.size = size;
        initLayout();
        initGrid();
        show();
        this.mainMenu = game;
    }

    private void initLayout() {
        ui = new HBox();

        Button left = getButton("<=", e -> this.shipDirection = setNextDirection(this.shipDirection));

        Button right = getButton("=>", e -> this.shipDirection = setPrevioustDirection(this.shipDirection));

        shipsLeft = new Label();
        setShipsLeftLabelText();

        Label l = new Label("Name:");

        playerName = new TextField();
        Button ready = new Button("Ready");
        ready.setDisable(true);
        playerName.textProperty().addListener((o, old, newV) -> {
            if (newV.length() > 0) {
                ready.setDisable(false);
            } else {
                ready.setDisable(true);
            }
        });
        ready.setOnAction(actionEvent -> ready());
        ui.getChildren().add(left);
        ui.getChildren().add(right);
        ui.getChildren().add(shipsLeft);
        ui.getChildren().add(l);
        ui.getChildren().add(playerName);
        ui.getChildren().add(ready);
        ui.setPadding(new Insets(0, 10, 10, 0));
        ui.setSpacing(10);
    }

    private void ready() {
        mainMenu.playerReady(playerName.getText(), setShips);
    }

    private Button getButton(String text, EventHandler<ActionEvent> listener) {
        Button right = new Button(text);
        setDefaultValues(right);
        right.setOnAction(listener);
        return right;
    }

    private void setShipsLeftLabelText() {
        shipsLeft.setText("Ships left: " + String.valueOf(shipsSize.size()));
    }

    private void setDefaultValues(Button button) {
        button.setPrefSize(30, 30);
        button.setMinSize(30, 30);
    }

    private void show() {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.getChildren().add(ui);
        root.getChildren().add(board);

        stage.setTitle(":D");
        stage.setOnCloseRequest(e -> System.exit(0));

        stage.setScene(new Scene(root, size * 50 + 50, size * 50 + 50));
        stage.show();
    }

    private void initGrid() {
        board = new GridPane();
        grid = new Button[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Button();
                Button button = grid[i][j];
                final int x = i;
                final int y = j;
                button.setOnMouseEntered(mouseDragEvent -> enterButton(x, y));
                button.setOnMouseExited(mouseDragEvent -> grid[x][y].setGraphic(new ImageView(empty)));
                button.setOnAction(actionEvent -> setNewShip(x, y));
                button.setGraphic(new ImageView(empty));
                button.setStyle("-fx-background-color: transparent");
                board.add(button, i, j);
            }
        }
    }

    private void setNewShip(int x, int y) {
        if (shipsSize.isEmpty()) {
            return;
        }
        if (validPosition) {
            List<Coordinates> newShip = new ArrayList<>();
            newShip.add(new Coordinates(x, y));
            int nextX = x;
            int nextY = y;
            for (int i = 1; i < activeShipSize; i++) {
                nextX += shipDirection.dx;
                nextY += shipDirection.dy;
                newShip.add(new Coordinates(nextX, nextY));
                if (nextX < 0 || nextX > size - 1 || nextY < 0 || nextY > size - 1) {
                    throw new IllegalArgumentException();
                }
            }
            setShips.add(newShip);
            activeShipSize = shipsSize.pop();
            setShipsLeftLabelText();
        }
    }

    private void enterButton(int x, int y) {
        resetGrid();
        if (!shipsSize.isEmpty()) {
            showActiveShip(x, y);
        }
    }

    private void showActiveShip(int x, int y) {
        Button button = grid[x][y];
        button.setGraphic(new ImageView(ship));
        int nextX = x;
        int nextY = y;
        for (int i = 1; i < activeShipSize; i++) {
            nextX += shipDirection.dx;
            nextY += shipDirection.dy;
            if (nextX < 0 || nextX > size - 1 || nextY < 0 || nextY > size - 1) {
                validPosition = false;
                return;
            }
            grid[nextX][nextY].setGraphic(new ImageView(ship));
        }
        validPosition = true;
    }

    private void resetGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j].setGraphic(new ImageView(empty));
            }
        }
        drawSetShips();
    }

    private void drawSetShips() {
        setShips.forEach(this::setShip);
    }

    private void setShip(List<Coordinates> coordinates) {
        coordinates.forEach(c -> grid[c.getX()][c.getY()].setGraphic(new ImageView(ship)));
    }

    private Direction setNextDirection(Direction direction) {
        switch (direction) {
            case UP:
                return Direction.RIGHT;
            case DOWN:
                return Direction.LEFT;
            case LEFT:
                return Direction.UP;
            case RIGHT:
                return Direction.DOWN;
            case NOT_SET:
                break;
        }
        return Direction.UP;
    }

    private Direction setPrevioustDirection(Direction direction) {
        switch (direction) {
            case UP:
                return Direction.LEFT;
            case DOWN:
                return Direction.RIGHT;
            case LEFT:
                return Direction.DOWN;
            case RIGHT:
                return Direction.UP;
            case NOT_SET:
                break;
        }
        return Direction.UP;
    }

    enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0), NOT_SET(0, 0);

        public int dx;
        public int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }
}
