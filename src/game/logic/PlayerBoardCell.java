package game.logic;


enum PlayerBoardCell {

    EMPTY(EnemyBoardCell.EMPTY) {
        @Override
        public PlayerBoardCell hit() {
            return EMPTY_HIT;
        }
    },

    SHIP(EnemyBoardCell.EMPTY) {
        @Override
        public PlayerBoardCell hit() {
            return SHIP_HIT;
        }
    },

    EMPTY_HIT(EnemyBoardCell.MISS) {
        @Override
        public PlayerBoardCell hit() {
            return this;
        }
    },

    SHIP_HIT(EnemyBoardCell.HIT) {
        @Override
        public PlayerBoardCell hit() {
            return this;
        }
    };

    private EnemyBoardCell isMappedTo;

    PlayerBoardCell(EnemyBoardCell isMappedTo) {
        this.isMappedTo = isMappedTo;
    }

    public EnemyBoardCell mapToEnemyBoardCell(){
        return isMappedTo;
    }

    public abstract PlayerBoardCell hit();
}
