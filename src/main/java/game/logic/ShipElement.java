package game.logic;

import game.api.Coordinates;
import lombok.Getter;

@Getter
class ShipElement {

    private Coordinates coordinates;
    private boolean hit = false;

    ShipElement(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    void hit() {
        hit = true;
    }

    boolean isNotHit() {
        return !hit;
    }
}
