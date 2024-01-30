
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

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

    public ArrayList<ConcretePiece> returnSizeBiggerThan2(ArrayList<ConcretePiece> list) {
        Iterator<ConcretePiece> iWin = list.iterator();
        while (iWin.hasNext()) {
            ConcretePiece piece = iWin.next();
            if (piece instanceof Pawn) {
                Pawn temp = (Pawn) piece;
                if (temp.getList().size() < 2) {
                    iWin.remove();
                }
            }
        }
        return list;
    }

    private int CompareBySizeOfLocations(boolean whoisWinner,ConcretePiece piece1, ConcretePiece piece2){
        int ans = 0;
        if(piece1 instanceof Pawn && piece2 instanceof Pawn ){
            Pawn temp1= (Pawn)piece1;
            Pawn temp2= (Pawn)piece2;
            if(temp1.getList().size() > temp2.getList().size())
                ans =1;
            if(temp1.getList().size() < temp2.getList().size())
                ans =-1;

        }
        return ans;
    }

    public ArrayList<ConcretePiece> returnDistanceBiggerthanZero(ArrayList<ConcretePiece> list) {
        Iterator<ConcretePiece> iWin = list.iterator();
        while (iWin.hasNext()) {
            ConcretePiece piece = iWin.next();
            if (piece instanceof Pawn) {
                Pawn temp = (Pawn) piece;
                if (temp.getDistance() < 1) {
                    iWin.remove();
                }
            }
            if (piece instanceof King) {
                King temp = (King) piece;
                if (temp.getDistance() < 1) {
                    iWin.remove();
                }
            }
        }
        return list;
    }
    public void printStats(boolean whoIsWinner){

        //print by locations first winner than loser
        Position posOfKing = getKingPosition();
        assert posOfKing != null;
        King k = (King)board[posOfKing.getpositionX()][posOfKing.getpositionY()];

        Comparator<ConcretePiece> comparetorbySizeOFLocations = new Comparator<ConcretePiece>() {
            @Override
            public int compare(ConcretePiece piece1, ConcretePiece piece2) {
                int ans = 0;
                if(piece1 instanceof Pawn && piece2 instanceof Pawn ){
                    Pawn temp1= (Pawn)piece1;
                    Pawn temp2= (Pawn)piece2;
                    if(temp1.getList().size() > temp2.getList().size())
                        ans =1;
                    if(temp1.getList().size() < temp2.getList().size())
                        ans =-1;

                }
                return ans;
            }
        };

        Comparator<ConcretePiece> comparetorDistance = new Comparator<ConcretePiece>() {
            @Override
            public int compare(ConcretePiece piece1, ConcretePiece piece2) {
                    int ans = 0;
                    if(piece1 instanceof Pawn && piece2 instanceof Pawn ){
                        Pawn temp1= (Pawn)piece1;
                        Pawn temp2= (Pawn)piece2;
                        if(temp1.getDistance() < temp2.getDistance())
                            ans =1;
                        if(temp1.getDistance() > temp2.getDistance())
                            ans =-1;

                    }

                    if(piece1 instanceof King && piece2 instanceof Pawn ){
                        King temp1= (King)piece1;
                        Pawn temp2= (Pawn)piece2;
                        if(temp1.getDistance() < temp2.getDistance())
                            ans =1;
                        if(temp1.getDistance() > temp2.getDistance())
                            ans =-1;

                    }

                    if(piece1 instanceof Pawn && piece2 instanceof King ){
                        Pawn temp1= (Pawn)piece1;
                        King temp2= (King)piece2;
                        if(temp1.getDistance() < temp2.getDistance())
                            ans =1;
                        if(temp1.getDistance() > temp2.getDistance())
                            ans =-1;

                    }
                    return ans;
                }

        };

        Comparator<ConcretePiece> compareByKills = new Comparator<ConcretePiece>() {
            @Override
            public int compare(ConcretePiece piece1, ConcretePiece piece2) {
                int ans = 0;
                if(piece1.getEatenPieces() > piece2.getEatenPieces()){
                    ans =1;
                }
                if(piece1.getEatenPieces() < piece2.getEatenPieces()){
                    ans =-1;
                }
                return ans;
            }
        };

        ArrayList<ConcretePiece> winner = new ArrayList<>();
        ArrayList<ConcretePiece> loser = new ArrayList<>();

        ArrayList<ConcretePiece> all = new ArrayList<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j] != null) {
                    if (board[i][j].getOwner().isPlayerOne() == whoIsWinner && board[i][j] instanceof Pawn) {
                        winner.add(board[i][j]);
                        all.add(board[i][j]);
                    }
                    if (board[i][j].getOwner().isPlayerOne() == !whoIsWinner && board[i][j] instanceof Pawn) {
                        loser.add(board[i][j]);
                        all.add(board[i][j]);
                    }
                }
            }

        }

        winner = returnSizeBiggerThan2(winner);
        winner.sort(comparetorbySizeOFLocations);
        if(!whoIsWinner){
            winner.add(k);
        }

        loser =  returnSizeBiggerThan2(loser);
        loser.sort(comparetorbySizeOFLocations);
        if(whoIsWinner){
            loser.add(k);
        }

        for (ConcretePiece temp : winner) {
            if (temp instanceof Pawn) {
                Pawn p = (Pawn) temp;
                p.printLocations();
            }
            if (temp instanceof King) {
                King k1 = (King) temp;
                k1.printLocations();
            }
        }

        for (ConcretePiece temp : loser) {
            if (temp instanceof Pawn) {
                Pawn p = (Pawn) temp;
                p.printLocations();
            }
            if (temp instanceof King) {
                King k1 = (King) temp;
                k1.printLocations();
            }
        }
        printcohav();
        all.add(k);
        all.sort(compareByKills);
        for (ConcretePiece temp : all) {
            if(temp.getEatenPieces() > 0) {
                if (temp instanceof Pawn) {
                    Pawn p = (Pawn) temp;
                    p.typeAndNumber();
                    System.out.println(" "+ temp.getEatenPieces() + " kills");
                }
                if (temp instanceof King) {
                    King k1 = (King) temp;
                    k1.typeAndNumber();
                    System.out.println(" "+ temp.getEatenPieces() + " kills");
                }
            }
        }


        printcohav();

        all.add(k);
        all = returnDistanceBiggerthanZero(all);
        all.sort(comparetorDistance);

        for (ConcretePiece temp : all) {
            if (temp instanceof Pawn) {
                Pawn p = (Pawn) temp;
                p.printDistance();
            }
            if (temp instanceof King) {
                King k1 = (King) temp;
                k1.printDistance();
            }
        }
        printcohav();

        for(int i = 0;i<BOARD_SIZE;i++){
            for(int j=0; j<BOARD_SIZE; j++){
                if(locations[i][j] > 1){
                    System.out.println(new Position(i,j).toString() +locations[i][j]+ " pieces" );
                }

            }
        }
        printcohav();

    }


    void printcohav(){
        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println("");
    }

    @Override
    public boolean move(Position a, Position b) {
        if (board[a.getpositionX()][a.getpositionY()].getOwner().isPlayerOne() == isAttackerNow) {
            if (legalMoovement(a, b)) {


                if (board[a.getpositionX()][a.getpositionY()] instanceof Pawn) {
                    Pawn temp = (Pawn) board[a.getpositionX()][a.getpositionY()];
                    ArrayList<Position> tempLocations= temp.getList();
                    Iterator i = tempLocations.iterator();
                    boolean flag = false;
                    while(i.hasNext()){
                        Position current = (Position) i.next();
                        if(current.getpositionX() == b.getpositionX() && current.getpositionY() == b.getpositionY()){
                            flag = true;
                            break;
                        }
                    }
                    if(!flag) locations[b.getpositionX()][b.getpositionY()]++;

                    int distance = getDistance(a, b);
                    temp.upDateDistance(distance);
                    temp.upDatePositions(b);
                    board[b.getpositionX()][b.getpositionY()] = board[a.getpositionX()][a.getpositionY()];
                    board[a.getpositionX()][a.getpositionY()] = null;


                }

                if (board[a.getpositionX()][a.getpositionY()] instanceof King) {
                    King temp = (King) board[a.getpositionX()][a.getpositionY()];
                    ArrayList<Position> tempLocations = temp.getList();
                    Iterator i = tempLocations.iterator();

                    boolean flag = false;
                    while(i.hasNext()){
                        Position current = (Position) i.next();
                        if(current.getpositionX() == b.getpositionX() && current.getpositionY() == b.getpositionY()){
                            flag = true;
                            break;
                        }
                    }
                    if(!flag) locations[b.getpositionX()][b.getpositionY()]++;
                    int distance = getDistance(a, b);
                    temp.upDateDistance(distance);
                    temp.upDatePositions(b);
                    board[b.getpositionX()][b.getpositionY()] = board[a.getpositionX()][a.getpositionY()];
                    board[a.getpositionX()][a.getpositionY()] = null;
                    locations[b.getpositionX()][b.getpositionY()]++;


                    if (isEdge(b.getpositionX(), b.getpositionY())) {// means the king get to the edge and defenders won
                        isGameFinished = true;
                        defender.updateWins();
                        printStats(false);
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
                if (board[kingX][kingY - 1].getOwner().isPlayerOne()) {
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
        }else {
            numOfThreats++;

            if (this.insideBoard(kingX,kingY - 1)&& board[kingX][kingY - 1] instanceof Pawn) {
                if (board[kingX][kingY - 1].getOwner().isPlayerOne()) {
                    numOfThreats++;
                }
            }
            if (this.insideBoard(kingX,kingY + 1)&&board[kingX][kingY + 1] instanceof Pawn) {
                if (board[kingX][kingY + 1].getOwner().isPlayerOne())
                    numOfThreats++;
            }
            if (this.insideBoard(kingX-1,kingY)&&board[kingX - 1][kingY] instanceof Pawn) {
                if (board[kingX - 1][kingY].getOwner().isPlayerOne())
                    numOfThreats++;
            }
            if (this.insideBoard(kingX+1,kingY)&&board[kingX + 1][kingY] instanceof Pawn) {
                if (board[kingX + 1][kingY].getOwner().isPlayerOne())
                    numOfThreats++;
            }}

        if (numOfThreats == 4) {
            this.isGameFinished = true;
            attacker.updateWins();
            printStats(true);
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
        return defender;
    }

    @Override
    public Player getSecondPlayer() {
        return attacker;
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
