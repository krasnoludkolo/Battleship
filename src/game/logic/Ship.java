package game.logic;

import game.Cell;
import game.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Ship {

    private final List<ShipElement> elements = new ArrayList<>();
    private boolean isSunk;

    Ship(List<Coordinates> coordinates) {
        coordinates.stream().map(ShipElement::new).forEach(elements::add);
        isSunk = false;
    }

    void hit(Coordinates c) {
        if (isPossibleToHit(c)) {
            ShipElement element = getShipElementAt(c);
            element.hit();
            checkIfSunk();
        }
    }

    boolean isHitAt(Coordinates c) {
        if(isPossibleToHit(c)){
            ShipElement shipElementAt = getShipElementAt(c);
            return shipElementAt.isHit();
        }
        return false;
    }

    boolean isPossibleToHit(Coordinates c) {
        return elements
                .stream()
                .map(ShipElement::getCoordinates)
                .collect(Collectors.toList())
                .contains(c);
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

    List<ShipElementDTO> getShipsElementDTOs(){
        if(elements.size()==1){
            Cell cell = isSunk ? Cell.SUNKEN_SHIP : Cell.ONE_CELL_SHIP;
            Coordinates coordinates = elements.get(0).getCoordinates();
            return Collections.singletonList(new ShipElementDTO(cell, coordinates));
        }

        if(isSunk){
            return elements
                    .stream()
                    .map(shipElement -> new ShipElementDTO(Cell.SUNKEN_SHIP,shipElement.getCoordinates()))
                    .collect(Collectors.toList());
        }

        //TODO do grafiki kieruki
        return elements
                .stream()
                .map(shipElement -> {
                    Cell cell = shipElement.isHit() ? Cell.HIT : Cell.SHIP;
                    return new ShipElementDTO(cell,shipElement.getCoordinates());
                })
                .collect(Collectors.toList());
    }
}
