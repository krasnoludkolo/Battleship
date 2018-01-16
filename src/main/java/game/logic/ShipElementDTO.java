package game.logic;

import game.api.Cell;
import game.api.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class ShipElementDTO {

    private Cell cells;
    private Coordinates coordinates;

}
