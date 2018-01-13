package game.logic;

import lombok.Getter;

@Getter
class BoardMoveResult {

    private boolean hit;
    private boolean sunk;

    private BoardMoveResult(boolean hit, boolean sunk) {
        this.hit = hit;
        this.sunk = sunk;
    }

    static BoardMoveResult hit(){
        return new BoardMoveResult(true,false);
    }

    static BoardMoveResult sunk(){
        return new BoardMoveResult(true,true);
    }

    static BoardMoveResult miss(){
        return new BoardMoveResult(false,false);
    }
}
