import java.util.ArrayList;

public class Pawn extends ConcretePiece {

    private ConcretePlayer owner;
    private Position currentPosition;
    private ArrayList<Position> locations;
    private static int eatenPieces = 0;
    private int distance;
    private int pawnNumber;


    public Pawn(ConcretePlayer owner, Position position, int pawnNumber) {
        this.owner = owner;
        this.currentPosition = position;
        this.locations = new ArrayList<Position>();
        locations.add(position);
        this.distance = 0;
        this.pawnNumber = pawnNumber;

    }

    public void upDatePositions(Position position) {
        currentPosition = position;
        locations.add(position);
    }

    public void upDateEatenPieces() {
        eatenPieces++;
    }


    @Override
    public ConcretePlayer getOwner() {
        return this.owner;
    }

    public String getType() {
        return this.getOwner().isPlayerOne() ? "♙" : "♟";
    }

    public void upDateDistance(int distance) {
        distance += distance;
    }


    public ArrayList<Position> getList() {
        return locations;

    }
}