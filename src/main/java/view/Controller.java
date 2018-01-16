package view;

import game.BattleshipGame;
import game.Cell;
import javafx.scene.image.Image;

abstract class Controller {

    protected BattleshipGame battleshipGame;
    protected String playerName;
    protected Image hit;
    protected Image empty;
    protected Image miss;
    protected Image ship;
    protected Image sunk;

    private String path = "resources/";

    public void init(String graphicName) {
        setGraphicSet(graphicName);
        initImages();
    }

    protected void initImages() {
        hit = new Image(getClass().getResourceAsStream(path + "hit.png"));
        empty = new Image(getClass().getResourceAsStream(path + "empty.png"));
        miss = new Image(getClass().getResourceAsStream(path + "miss.png"));
        ship = new Image(getClass().getResourceAsStream(path + "ship.png"));
        sunk = new Image(getClass().getResourceAsStream(path + "sunk.png"));
    }

    void setGraphicSet(String name) {
        path += name + "/";
    }

    protected Image getImageFor(Cell cell) {
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
}
