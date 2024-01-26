import java.security.PrivateKey;
import java.util.Arrays;

public class GameLogic implements PlayableLogic {
    private final int BOARD_SIZE = 11;
    private ConcretePiece[][] board;
    private int[][] locations;
    private ConcretePlayer attacker;
    private ConcretePlayer defender;
    private boolean isAttackerNow;
    private boolean isGameFinished;

    public GameLogic() {
        this.board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        attacker = new ConcretePlayer(true);
        defender = new ConcretePlayer(false);
        this.reset();
    }

    public int getDistance(Position a, Position b) {
        return Math.abs(a.getpositionX() - b.getpositionX() + a.getpositionY() - b.getpositionY());
    }

    public boolean legalMoovement(Position a, Position b) {
        int aXposition = a.getpositionX();
        int aYposition = a.getpositionY();
        int bXposition = b.getpositionX();
        int bYposition = b.getpositionY();

        //if there is a piece
        if (getPieceAtPosition(b) != null)
            return false;
        //same place
        if (aXposition == bXposition && aYposition == bYposition)
            return false;

        //off board
        if (bYposition < 0 || bYposition > 10 || bXposition < 0 || bXposition > 10)
            return false;
        //diagonal
        if (a.getpositionX() != b.getpositionX() && a.getpositionY() != b.getpositionY())
            return false;
        //Edge forbidden for pawn
        ConcretePiece pieceType = board[a.getpositionX()][a.getpositionY()];
        if (pieceType instanceof Pawn && (this.isEdge(b)))
            return false;
        //if trying to pass/step on another piece

        if (aXposition != bXposition) {
            for (int i = Math.min(aXposition, bXposition) + 1; i <= Math.max(aXposition, bXposition) - 1; i++) {
                if (board[i][aYposition] != null) {
                    return false;
                }
            }
        }
        for (int i = Math.min(aYposition, bYposition) + 1; i <= Math.max(aYposition, bYposition) - 1; i++) {
            if (board[aXposition][i] != null) {
                return false;
            }
        }

        return true;

    }

    @Override
    public boolean move(Position a, Position b) {
        if (board[a.getpositionX()][a.getpositionY()].getOwner().isPlayerOne() == isAttackerNow) {
            if (legalMoovement(a, b)) {

                if (board[a.getpositionX()][a.getpositionY()] instanceof Pawn) {
                    Pawn temp = (Pawn) board[a.getpositionX()][a.getpositionY()];
                    int distance = getDistance(a, b);
                    temp.upDateDistance(distance);
                    temp.upDatePositions(b);
                    board[b.getpositionX()][b.getpositionY()] = board[a.getpositionX()][a.getpositionY()];
                    board[a.getpositionX()][a.getpositionY()] = null;
                }

                if (board[a.getpositionX()][a.getpositionY()] instanceof King) {
                    King temp = (King) board[a.getpositionX()][a.getpositionY()];
                    int distance = getDistance(a, b);
                    temp.upDateDistance(distance);
                    temp.upDatePositions(b);
                    board[b.getpositionX()][b.getpositionY()] = board[a.getpositionX()][a.getpositionY()];
                    board[a.getpositionX()][a.getpositionY()] = null;


                    if (isEdge(b.getpositionX(), b.getpositionX())) {// means the king get to the edge and defenders won
                        isGameFinished = true;
                        defender.updateWins();
                        this.reset();
                    }

                }
                isAttackerNow = !isAttackerNow;
                this.losingCheck();
                this.eatingCheck(board[b.getpositionX()][b.getpositionY()], b);
                return true;
            }


        }
        //check if ConcretePiece has eaten another piece in Position b and increasing eatenPieces field


        return false;
    }

    public boolean isEdge(Position a) {
        return (a.getpositionX() == 0 || a.getpositionX() == 10) && (a.getpositionY() == 0 || a.getpositionY() == 10);
    }

    public boolean isEdge(int x, int y) {
        if ((x == 0 || x == 10) && (y == 0 || y == 10))
            return true;
        return false;

    }



    public void eatingCheck(ConcretePiece piece, Position a) {
        int xPiece = a.getpositionX();
        int yPiece = a.getpositionY();

        int yOneDown = yPiece - 1; // yOneDown
        int xOneLeft = xPiece - 1; //xOneLeft
        int yOneUp = yPiece + 1;// yOneUp
        int xOneRight = xPiece + 1; //xOneRight




            //case 1: eating from right:

            if (insideBoard(xOneRight, yPiece) && !(board[xOneRight][yPiece] instanceof King)) {
                if (!insideBoard(xOneRight + 1, yPiece) && board[xOneRight][yPiece] != null) {
                    if (board[xOneRight][yPiece].getOwner() != piece.getOwner()) {
                        piece.upDateEatenPieces();
                        board[xOneRight][yPiece] = null;
                    }
                }
                if (insideBoard(xOneRight + 1, yPiece) && board[xOneRight][yPiece] != null && board[xOneRight + 1][yPiece] != null) {
                    if (board[xOneRight][yPiece].getOwner() != piece.getOwner() && (board[xOneRight + 1][yPiece].getOwner() == piece.getOwner()
                    )) {
                        piece.upDateEatenPieces();
                        board[xOneRight][yPiece] = null;
                    }
                }
                if (this.isEdge(xOneRight + 1, yPiece) && board[xOneRight][yPiece] != null && board[xOneRight][yPiece].getOwner() != piece.getOwner()) {
                    piece.upDateEatenPieces();
                    board[xOneRight][yPiece] = null;
                }
            }

            //case 2: eating from left:

            if (insideBoard(xOneLeft, yPiece) && !(board[xOneLeft][yPiece] instanceof King)) {
                if (!insideBoard(xOneLeft - 1, yPiece) && board[xOneLeft][yPiece] != null) {
                    if (board[xOneLeft][yPiece].getOwner() != piece.getOwner()) {
                        piece.upDateEatenPieces();
                        board[xOneLeft][yPiece] = null;
                    }
                }
                if (insideBoard(xOneLeft - 1, yPiece) && board[xOneLeft][yPiece] != null && board[xOneLeft - 1][yPiece] != null) {
                    if (board[xOneLeft][yPiece].getOwner() != piece.getOwner() && (board[xOneLeft - 1][yPiece].getOwner() == piece.getOwner()
                    )) {
                        piece.upDateEatenPieces();
                        board[xOneLeft][yPiece] = null;
                    }
                }
                if (this.isEdge(xOneLeft - 1, yPiece) && board[xOneLeft][yPiece] != null && board[xOneLeft][yPiece].getOwner() != piece.getOwner()) {
                    piece.upDateEatenPieces();
                    board[xOneLeft][yPiece] = null;
                }
            }
            // case 3: eating from down:

            if (insideBoard(xPiece, yOneDown) && !(board[xPiece][yOneDown] instanceof King)) {
                if (!insideBoard(xPiece, yOneDown - 1) && board[xPiece][yOneDown] != null) {
                    if (board[xPiece][yOneDown].getOwner() != piece.getOwner()) {
                        piece.upDateEatenPieces();
                        board[xPiece][yOneDown] = null;
                    }
                }
                if (insideBoard(xPiece, yOneDown - 1) && board[xPiece][yOneDown] != null && board[xPiece][yOneDown - 1] != null) {
                    if (board[xPiece][yOneDown].getOwner() != piece.getOwner() && (board[xPiece][yOneDown - 1].getOwner() == piece.getOwner()
                    )) {
                        piece.upDateEatenPieces();
                        board[xPiece][yOneDown] = null;
                    }
                }
                if (this.isEdge(xPiece, yOneDown - 1) && board[xPiece][yOneDown] != null && board[xPiece][yOneDown].getOwner() != piece.getOwner()) {
                    piece.upDateEatenPieces();
                    board[xPiece][yOneDown] = null;
                }
            }
            // case 4: eating from up:

            if (insideBoard(xPiece, yOneUp) && !(board[xPiece][yOneUp] instanceof King)) {
                if (!insideBoard(xPiece, yOneUp + 1) && board[xPiece][yOneUp] != null) {
                    if (board[xPiece][yOneUp].getOwner() != piece.getOwner()) {
                        piece.upDateEatenPieces();
                        board[xPiece][yOneUp] = null;
                    }
                }
                if (insideBoard(xPiece, yOneUp + 1) && board[xPiece][yOneUp] != null && board[xPiece][yOneUp + 1] != null) {
                    if (board[xPiece][yOneUp].getOwner() != piece.getOwner() && (board[xPiece][yOneUp + 1].getOwner() == piece.getOwner()
                    )) {
                        piece.upDateEatenPieces();
                        board[xPiece][yOneUp] = null;
                    }
                }
                if (this.isEdge(xPiece, yOneUp + 1) && board[xPiece][yOneUp] != null && board[xPiece][yOneUp].getOwner() != piece.getOwner()) {
                    piece.upDateEatenPieces();
                    board[xPiece][yOneUp] = null;
                }
            }

    }

    private boolean insideBoard(int x, int y) {
        return x >= 0 && x < this.getBoardSize() && y >= 0 && y < this.getBoardSize();
    }

    public void losingCheck() {
        int numOfThreats = 0;
        Position kingPosition = getKingPosition();
        int kingX = kingPosition.getpositionX();
        int kingY = kingPosition.getpositionY();

        if (kingX - 1 >= 0 && kingX + 1 < this.getBoardSize() && kingY - 1 >= 0 && kingY + 1 < this.getBoardSize()) {
            if (board[kingX][kingY - 1] instanceof Pawn) {
                if (board[kingX][kingY - 1].getOwner().isPlayerOne() == true) {
                    numOfThreats++;
                }
            }
            if (board[kingX][kingY + 1] instanceof Pawn) {
                if (board[kingX][kingY + 1].getOwner().isPlayerOne())
                    numOfThreats++;
            }
            if (board[kingX - 1][kingY] instanceof Pawn) {
                if (board[kingX - 1][kingY].getOwner().isPlayerOne())
                    numOfThreats++;
            }
            if (board[kingX + 1][kingY] instanceof Pawn) {
                if (board[kingX + 1][kingY].getOwner().isPlayerOne())
                    numOfThreats++;
            }
        }

        if (numOfThreats == 4) {
            this.isGameFinished = true;
            attacker.updateWins();
            this.reset();

        }
        //add corners
    }

    private Position getKingPosition() {
        for (int i = 0; i < this.getBoardSize(); i++) {
            for (int j = 0; j < this.getBoardSize(); j++) {
                if (board[i][j] instanceof King)
                    return new Position(i, j);
            }
        }
        return null;
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
        return isAttackerNow;
    }

    public void reset() {
        locations = new int[BOARD_SIZE][BOARD_SIZE];
        isAttackerNow = true;
        isGameFinished = false;

        for (int i = 0; i < BOARD_SIZE; i++) {
            Arrays.fill(board[i], null);
        }

        //attacker pawns
        int pawnNumber = 1;
        for (int i = 3; i <= 7; i++) {
            board[i][0] = new Pawn(attacker, new Position(i, 0), pawnNumber);
            pawnNumber++;
        }
        board[5][1] = new Pawn(attacker, new Position(5, 1), pawnNumber);
        pawnNumber++;
        board[0][3] = new Pawn(attacker, new Position(0, 3), pawnNumber);
        pawnNumber++;
        board[10][3] = new Pawn(attacker, new Position(10, 3), pawnNumber);
        pawnNumber++;
        board[0][4] = new Pawn(attacker, new Position(0, 4), pawnNumber);
        pawnNumber++;
        board[10][4] = new Pawn(attacker, new Position(10, 4), pawnNumber);
        pawnNumber++;
        board[0][5] = new Pawn(attacker, new Position(0, 5), pawnNumber);
        pawnNumber++;
        board[1][5] = new Pawn(attacker, new Position(1, 5), pawnNumber);
        pawnNumber++;
        board[9][5] = new Pawn(attacker, new Position(9, 5), pawnNumber);
        pawnNumber++;
        board[10][5] = new Pawn(attacker, new Position(10, 5), pawnNumber);
        pawnNumber++;
        board[0][6] = new Pawn(attacker, new Position(0, 6), pawnNumber);
        pawnNumber++;
        board[10][6] = new Pawn(attacker, new Position(10, 6), pawnNumber);
        pawnNumber++;
        board[0][7] = new Pawn(attacker, new Position(0, 7), pawnNumber);
        pawnNumber++;
        board[10][7] = new Pawn(attacker, new Position(10, 7), pawnNumber);
        pawnNumber++;
        board[5][9] = new Pawn(attacker, new Position(5, 9), pawnNumber);
        pawnNumber++;
        for (int i = 3; i <= 7; i++) {
            board[i][10] = new Pawn(attacker, new Position(i, 0), pawnNumber);
            pawnNumber++;
        }

        //defender pieces
        board[5][5] = new King(defender, new Position(5, 5));
        pawnNumber = 1;
        board[5][3] = new Pawn(defender, new Position(5, 3), pawnNumber);
        pawnNumber++;
        board[4][4] = new Pawn(defender, new Position(4, 4), pawnNumber);
        pawnNumber++;
        board[5][4] = new Pawn(defender, new Position(5, 4), pawnNumber);
        pawnNumber++;
        board[6][4] = new Pawn(defender, new Position(6, 4), pawnNumber);
        pawnNumber++;
        board[3][5] = new Pawn(defender, new Position(3, 5), pawnNumber);
        pawnNumber++;
        board[4][5] = new Pawn(defender, new Position(4, 5), pawnNumber);
        pawnNumber++;
        pawnNumber++;
        board[6][5] = new Pawn(defender, new Position(6, 5), pawnNumber);
        pawnNumber++;
        board[7][5] = new Pawn(defender, new Position(7, 5), pawnNumber);
        pawnNumber++;
        board[4][6] = new Pawn(defender, new Position(4, 6), pawnNumber);
        pawnNumber++;
        board[5][6] = new Pawn(defender, new Position(5, 6), pawnNumber);
        pawnNumber++;
        board[6][6] = new Pawn(defender, new Position(6, 6), pawnNumber);
        pawnNumber++;
        board[5][7] = new Pawn(defender, new Position(5, 7), pawnNumber);

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
