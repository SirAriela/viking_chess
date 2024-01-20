public class GameLogic implements PlayableLogic {
    private final int BOARD_SIZE = 11;
    private ConcretePiece[][] board;
    private int[][] locations;
    private ConcretePlayer attacker;
    private ConcretePlayer defender;
    private boolean isAttackerNow;
    private boolean isGameFinished;

    public GameLogic(){
        this.board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        this.reset();
    }

    public int getDistance(Position a, Position b) {
        return Math.abs(a.getpositionX() - b.getpositionX() + a.getpositionY() - b.getpositionY());
    }
    public boolean legalMoovement (Position a, Position b) {
        int aXposition = a.getpositionX();
        int aYposition = a.getpositionY();
        int bXposition = b.getpositionX();
        int bYposition = b.getpositionY();

        //if trying to move on another piece
        if (getPieceAtPosition(b)!= null)
            return false;
        //same place
        if (aXposition == bXposition && aYposition==bYposition)
            return false;
        //off board
        if (bYposition < 0 || bYposition > 10 || bXposition < 0 || bXposition > 10)
            return false;
        //diagnal
        if(a.getpositionX() != b.getpositionX() && a.getpositionY() != b.getpositionY())
            return false;
        //need to be only for pawn
//        if (bYposition == 0 && (bXposition == BOARD_SIZE-1 || bXposition == 0) || bYposition == BOARD_SIZE-1 && (bXposition == BOARD_SIZE-1 || bXposition == 0))
//            return false;
//
        return true;
    }
    @Override
    public boolean move(Position a, Position b) {
        if (legalMoovement(a, b)) {
            if (board[a.getpositionX()][a.getpositionY()] instanceof Pawn) {
                Pawn temp =(Pawn)board[a.getpositionX()][a.getpositionY()];
                int distance = getDistance(a,b);
                temp.upDateDistance(distance);
                temp.upDatePositions(b);

            }
            if (board[b.getpositionX()][b.getpositionY()] instanceof King) {
                King temp =(King)board[b.getpositionX()][b.getpositionY()];
                int distance = getDistance(a,b);
                temp.upDateDistance(distance);
                temp.upDatePositions(b);
            }

            board[b.getpositionX()][b.getpositionY()] = board[a.getpositionX()][a.getpositionY()];
            board[a.getpositionX()][a.getpositionY()] = null;

            return true;
        }
        return false;

    }

    @Override
    public Piece getPieceAtPosition(Position position) {
       int x = position.getpositionX();
       int y = position.getpositionY();
       return this.board[x][y];
    }

    @Override
    public Player getFirstPlayer() {
        return attacker;
    }

    @Override
    public Player getSecondPlayer() {
        return defender;
    }

    @Override
    public boolean isGameFinished() {
        return this.isGameFinished;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return false;
    }

    @Override
    public void reset() {
        attacker = new ConcretePlayer(true);
        defender = new ConcretePlayer(false);
        locations = new int[BOARD_SIZE][BOARD_SIZE];
        isAttackerNow = true;
        isGameFinished = false;

        //attacker pawns
        int pawnNumber = 1;
        for (int i = 3; i < 7; i++) {
            board[0][i] = new Pawn(attacker, new Position(0, i), pawnNumber);
            pawnNumber++;
        }
        board[1][5] = new Pawn(attacker, new Position(1, 5), pawnNumber);
        pawnNumber++;
        board[3][0] = new Pawn(attacker, new Position(3, 0), pawnNumber);
        pawnNumber++;
        board[3][10] = new Pawn(attacker, new Position(3, 10), pawnNumber);
        pawnNumber++;
        board[4][0] = new Pawn(attacker, new Position(4, 0), pawnNumber);
        pawnNumber++;
        board[4][10] = new Pawn(attacker, new Position(4, 10), pawnNumber);
        pawnNumber++;
        board[5][0] = new Pawn(attacker, new Position(5, 0), pawnNumber);
        pawnNumber++;
        board[5][1] = new Pawn(attacker, new Position(5, 1), pawnNumber);
        pawnNumber++;
        board[5][9] = new Pawn(attacker, new Position(5, 9), pawnNumber);
        pawnNumber++;
        board[5][10] = new Pawn(attacker, new Position(5, 10), pawnNumber);
        pawnNumber++;
        board[6][0] = new Pawn(attacker, new Position(6, 0), pawnNumber);
        pawnNumber++;
        board[6][10] = new Pawn(attacker, new Position(6, 10), pawnNumber);
        pawnNumber++;
        board[7][0] = new Pawn(attacker, new Position(7, 0), pawnNumber);
        pawnNumber++;
        board[7][10] = new Pawn(attacker, new Position(7, 10), pawnNumber);
        pawnNumber++;
        board[9][5] = new Pawn(attacker, new Position(9, 5), pawnNumber);
        pawnNumber++;
        for (int i = 3; i < 7; i++) {
            board[10][i] = new Pawn(attacker, new Position(0, i), pawnNumber);
            pawnNumber++;
        }

        //defender pieces
        board[5][5] = new King(defender, new Position(5, 5));
        pawnNumber = 1;
        board[3][5] = new Pawn(defender, new Position(3, 5), pawnNumber);
        pawnNumber++;
        board[4][4] = new Pawn(defender, new Position(4, 4), pawnNumber);
        pawnNumber++;
        board[4][5] = new Pawn(defender, new Position(4, 5), pawnNumber);
        pawnNumber++;
        board[4][6] = new Pawn(defender, new Position(4, 6), pawnNumber);
        pawnNumber++;
        board[5][3] = new Pawn(defender, new Position(5, 3), pawnNumber);
        pawnNumber++;
        board[5][4] = new Pawn(defender, new Position(5, 4), pawnNumber);
        pawnNumber++;
        pawnNumber++;
        board[5][6] = new Pawn(defender, new Position(5, 6), pawnNumber);
        pawnNumber++;
        board[5][7] = new Pawn(defender, new Position(5, 7), pawnNumber);
        pawnNumber++;
        board[6][4] = new Pawn(defender, new Position(6, 4), pawnNumber);
        pawnNumber++;
        board[6][5] = new Pawn(defender, new Position(6, 5), pawnNumber);
        pawnNumber++;
        board[6][6] = new Pawn(defender, new Position(6, 6), pawnNumber);
        pawnNumber++;
        board[7][5] = new Pawn(defender, new Position(7, 5), pawnNumber);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null)
                    locations[i][j] = 1;
            }
        }
    }

    @Override
    public void undoLastMove() {

    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }
}
