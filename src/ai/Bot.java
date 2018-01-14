package ai;

import game.ActionResult;
import game.BattleshipGame;
import game.Cell;
import game.Coordinates;

public class Bot extends AbstractBot {

    private State state = State.SEARCHING;
    private Direction direction = Direction.NOT_SET;
    private Coordinates lastShot;
    private Coordinates firstHit;
    private boolean knownDirection;


    public Bot(BattleshipGame battleshipGame, String name) {
        super(battleshipGame, name, 100);
    }

    @Override
    protected Coordinates nextMoveCoordinates() {
        Coordinates coordinates = getCoordinates();
        lastShot = coordinates;
        return coordinates;
    }

    private Coordinates getCoordinates() {
        if (firstMove()) {
            return getRandomCoordinatesButNotNextToShip();
        }
        if (state == State.HUNTING && lastMoveResult.getActionResult() == ActionResult.HIT) {
            knownDirection = true;
        }
        if (lastMoveResult.getActionResult() == ActionResult.HIT && state != State.HUNTING) {
            firstHit = lastShot;
            state = State.HUNTING;
        }
        if (lastMoveResult.getActionResult() == ActionResult.SUNK) {
            state = State.SEARCHING;
            direction = Direction.NOT_SET;
            knownDirection = false;
        }

        if (state == State.SEARCHING) {
            return getRandomCoordinatesButNotNextToShip();
        }

        if (state == State.HUNTING && direction == Direction.NOT_SET) {
            direction = Direction.UP;
        }
        while (nextMoveWillBeOutBoard()) {
            setNextDirection();
        }

        if (state == State.HUNTING && lastMoveResult.getActionResult() == ActionResult.MISS) {
            if (knownDirection) {
                setOppositeDirection();
                return nextFromFirstHit();
            } else {
                setNextDirection();
            }
            return coordinatesWithDirection();
        }

        if (state == State.HUNTING) {
            return coordinatesWithDirection();
        }
        return getRandomCoordinatesButNotNextToShip();
    }

    private Coordinates nextFromFirstHit() {
        int x = firstHit.getX() + direction.dx;
        int y = firstHit.getY() + direction.dy;
        return new Coordinates(x, y);
    }

    private Coordinates coordinatesWithDirection() {
        int x = lastShot.getX() + direction.dx;
        int y = lastShot.getY() + direction.dy;
        return new Coordinates(x, y);
    }

    private void setNextDirection() {
        switch (direction) {
            case UP:
                direction = Direction.DOWN;
                break;
            case DOWN:
                direction = Direction.LEFT;
                break;
            case LEFT:
                direction = Direction.RIGHT;
                break;
            case RIGHT:
                direction = Direction.UP;
                break;
            case NOT_SET:
                break;
        }
    }

    private void setOppositeDirection() {
        switch (direction) {
            case UP:
                direction = Direction.DOWN;
                break;
            case DOWN:
                direction = Direction.UP;
                break;
            case LEFT:
                direction = Direction.RIGHT;
                break;
            case RIGHT:
                direction = Direction.LEFT;
                break;
            case NOT_SET:
                break;
        }
    }

    private boolean nextMoveWillBeOutBoard() {
        int x = lastShot.getX() + direction.dx;
        int y = lastShot.getY() + direction.dy;
        int size = lastGameStatus.getEnemyBoardCells().length;

        return x < 0 || y < 0 || x >= size || y >= size;
    }

    private boolean firstMove() {
        return lastMoveResult == null;
    }

    public Coordinates getRandomCoordinatesButNotNextToShip() {
        Coordinates randomCoordinates;
        do {
            randomCoordinates = getRandomCoordinates();
        } while (isNextToShip(randomCoordinates));
        lastShot = randomCoordinates;
        return randomCoordinates;
    }

    private boolean isNextToShip(Coordinates randomCoordinates) {
        Cell[][] board = lastGameStatus.getEnemyBoardCells();
        int size = board.length;
        int x = randomCoordinates.getX();
        int y = randomCoordinates.getY();

        int plusX = Math.min(x + 1, size - 1);
        int plusY = Math.min(y + 1, size - 1);
        int minusX = Math.max(x - 1, 0);
        int minusY = Math.max(y - 1, 0);

        return board[x][plusY] == Cell.SHIP || board[x][plusY] == Cell.SUNKEN_SHIP
                || board[x][minusY] == Cell.SHIP || board[x][minusY] == Cell.SUNKEN_SHIP
                || board[plusX][y] == Cell.SHIP || board[plusX][y] == Cell.SUNKEN_SHIP
                || board[minusX][y] == Cell.SHIP || board[minusX][y] == Cell.SUNKEN_SHIP;
    }


    enum State {
        HUNTING, SEARCHING
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
