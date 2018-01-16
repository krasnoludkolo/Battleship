package game.logic;

import game.api.Cell;
import game.api.Coordinates;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Ship {

    private final List<ShipElement> elements;
    private boolean isSunk;

    Ship(List<Coordinates> coordinates) {
        elements = coordinates.stream().map(ShipElement::new).collect(Collectors.toList());
        isSunk = false;
    }

    void hit(Coordinates c) {
        if (hasElementAt(c)) {
            ShipElement element = getShipElementAt(c);
            element.hit();
            checkIfSunk();
        }
    }

    boolean isHitAt(Coordinates c) {
        if (hasElementAt(c)) {
            ShipElement shipElementAt = getShipElementAt(c);
            return shipElementAt.isHit();
        }
        return false;
    }

    boolean hasElementAt(Coordinates c) {
        for (ShipElement element : elements) {
            if (element.getCoordinates().equals(c)) {
                return true;
            }
        }
        return false;
    }


    private void checkIfSunk() {
        int hitElements = elements.stream().mapToInt(e -> e.isHit() ? 1 : 0).sum();
        isSunk = hitElements == elements.size();
    }

    private ShipElement getShipElementAt(Coordinates c) {
        for (ShipElement e : elements) {
            if (e.getCoordinates().equals(c)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No ship element");
    }

    boolean isSunk() {
        return isSunk;
    }

    List<ShipElementDTO> getShipsElementDTOs() {
        if (elements.size() == 1) {
            Cell cell = isSunk ? Cell.SUNKEN_SHIP : Cell.ONE_CELL_SHIP;
            Coordinates coordinates = elements.get(0).getCoordinates();
            return Collections.singletonList(new ShipElementDTO(cell, coordinates));
        }

        if (isSunk) {
            return elements
                    .stream()
                    .map(shipElement -> new ShipElementDTO(Cell.SUNKEN_SHIP, shipElement.getCoordinates()))
                    .collect(Collectors.toList());
        }

        //TODO do grafiki kieruki
        return elements
                .stream()
                .map(shipElement -> {
                    Cell cell = shipElement.isHit() ? Cell.HIT : Cell.SHIP;
                    return new ShipElementDTO(cell, shipElement.getCoordinates());
                })
                .collect(Collectors.toList());
    }
}
