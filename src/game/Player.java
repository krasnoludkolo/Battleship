package game;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Player {

    private final String name;
    private final List<List<Coordinates>> ships;

}
