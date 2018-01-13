package game.logic;

import game.Cell;
import game.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class ShipElementDTO {

    private Cell cells;
    private Coordinates coordinates;

}
