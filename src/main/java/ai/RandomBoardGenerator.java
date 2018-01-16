package ai;

import game.api.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class RandomBoardGenerator {

    public static List<List<Coordinates>> getRandom(Stack<Integer> shipsSizes, int boardSize) {
        return Collections.emptyList();
    }

    public static List<List<Coordinates>> getTestBoard() {
        List<List<Coordinates>> list = new ArrayList<>();

        for (int i = 1; i < 7; i += 2) {
            List<Coordinates> ship = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                Coordinates c = new Coordinates(i, j);
                ship.add(c);
            }
            list.add(ship);
        }
        for (int i = 2; i < 8; i += 2) {
            List<Coordinates> ship = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                Coordinates c = new Coordinates(j, i);
                ship.add(c);
            }
            list.add(ship);
        }
        return list;
    }
}
