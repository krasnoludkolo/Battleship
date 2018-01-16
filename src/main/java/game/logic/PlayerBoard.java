package game.logic;

import game.api.Cell;
import game.api.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class PlayerBoard {

    private List<Ship> ships;
    private int size;
    private final List<Cell> SHIPS_PARTS = Arrays.asList(Cell.END_SHIP_DOWN, Cell.END_SHIP_LEFT, Cell.END_SHIP_RIGHT, Cell.END_SHIP_UP, Cell.ONE_CELL_SHIP, Cell.SHIP);
    private final List<Coordinates> misses = new ArrayList<>();

    PlayerBoard(List<List<Coordinates>> ships, int size) {
        this.ships = ships.stream().map(Ship::new).collect(Collectors.toList());
        this.size = size;
    }

    BoardMoveResult hit(final Coordinates coordinates) {
        //TODO brzydko
        Optional<Ship> shipAt = getShipAt(coordinates);
        if (shipAt.isPresent()) {
            Ship ship = shipAt.get();
            ship.hit(coordinates);
            if (ship.isSunk()) {
                return BoardMoveResult.sunk();
            }
            return BoardMoveResult.hit();
        }
        misses.add(coordinates);
        return BoardMoveResult.miss();
    }

    private Optional<Ship> getShipAt(Coordinates coordinates) {
        for (Ship ship : ships) {
            if (ship.hasElementAt(coordinates)) {
                return Optional.of(ship);
            }
        }
        return Optional.empty();
    }

    Cell[][] getCellsArrayAsOwner() {
        final Cell[][] cells = getEmptyCellsBoardWithMisses();
        ships.forEach(ship -> {
            List<ShipElementDTO> shipsElementDTOs = ship.getShipsElementDTOs();
            shipsElementDTOs.forEach(dto -> {
                int x = dto.getCoordinates().getX();
                int y = dto.getCoordinates().getY();
                cells[x][y] = dto.getCells();
            });
        });
        return cells;
    }

    Cell[][] getCellsArrayAsEnemy() {
        final Cell[][] cells = getEmptyCellsBoardWithMisses();
        ships.forEach(ship -> {
            List<ShipElementDTO> shipsElementDTOs = ship.getShipsElementDTOs();
            shipsElementDTOs.forEach(dto -> {
                int x = dto.getCoordinates().getX();
                int y = dto.getCoordinates().getY();
                Cell cell = dto.getCells();
                if (isShip(cell)) {
                    cell = Cell.EMPTY;
                }
                cells[x][y] = cell;
            });
        });
        return cells;
    }

    private boolean isShip(Cell cell) {
        return SHIPS_PARTS.contains(cell);
    }

    private Cell[][] getEmptyCellsBoardWithMisses() {
        final Cell[][] cells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = Cell.EMPTY;
            }
        }
        misses.forEach(c -> cells[c.getX()][c.getY()] = Cell.MISS);
        return cells;
    }

    boolean moveOutOfBoard(Coordinates coordinates) {
        int x = coordinates.getX();
        int y = coordinates.getY();
        return x < 0 || y < 0 || x >= size || y >= size;
    }

    boolean isPlaceAlreadyToHit(Coordinates coordinates) {
        Optional<Ship> shipAt = getShipAt(coordinates);
        return shipAt.map(ship -> ship.isHitAt(coordinates)).orElse(false);
    }

    boolean isPlaceAlreadyMiss(Coordinates coordinates) {
        return misses.contains(coordinates);
    }

    public boolean hasShipsLeft() {
        return ships.stream().mapToInt(ship -> ship.isSunk() ? 0 : 1).sum() > 0;
    }
}
