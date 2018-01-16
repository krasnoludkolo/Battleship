package game.logic;

import game.api.ActionResult;
import game.api.Coordinates;
import game.api.MoveResult;
import game.api.NewPlayer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestBattleshipGameTest {

    private NewPlayer newPlayerA = new NewPlayer("test1", testList());
    private NewPlayer newPlayerB = new NewPlayer("test2", testList());

    @Test
    void shouldBeMiss() {
        RestBattleshipGame game = new RestBattleshipGame(newPlayerA, newPlayerB, 10);
        Coordinates coordinates = new Coordinates(2, 2);
        MoveResult moveResult = game.makeMove("test1", coordinates);
        ActionResult shouldBeMiss = moveResult.getActionResult();
        assertEquals(ActionResult.MISS, shouldBeMiss);
    }

    @Test
    void shouldBeSunk() {
        RestBattleshipGame game = new RestBattleshipGame(newPlayerA, newPlayerB, 10);
        Coordinates coordinates = new Coordinates(1, 0);
        MoveResult moveResult = game.makeMove("test1", coordinates);
        ActionResult shouldBeSunk = moveResult.getActionResult();
        assertEquals(ActionResult.SUNK, shouldBeSunk);
    }

    @Test
    void shouldBeHitMiss() {
        RestBattleshipGame game = new RestBattleshipGame(newPlayerA, newPlayerB, 10);
        Coordinates coordinates = new Coordinates(2, 2);
        game.makeMove("test1", coordinates);
        game.makeMove("test2", coordinates);
        MoveResult moveResult = game.makeMove("test1", coordinates);
        ActionResult shouldBeHitMiss = moveResult.getActionResult();
        assertEquals(ActionResult.HIT_MISS, shouldBeHitMiss);
    }

    private List<List<Coordinates>> testList() {
        List<List<Coordinates>> ships = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Coordinates> ship = Collections.singletonList(new Coordinates(1, i * 2));
            ships.add(ship);
        }
        return ships;
    }

}