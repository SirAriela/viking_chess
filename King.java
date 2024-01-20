import java.util.ArrayList;

public class King extends ConcretePiece {
    private ConcretePlayer owner;

    private Position currentPosition;
    private ArrayList<Position> locations;
    private static int eatenPieces = 0;
    private int distance;

    public King(ConcretePlayer owner, Position position) {
        this.owner =owner;
        this.currentPosition = position;
        this.locations = new ArrayList<Position>();
        locations.add(position);
        this.distance = 0;
    }

    public void upDatePositions(Position position) {
        currentPosition = position;
        locations.add(position);
    }

    public void upDateDistance(int distance) {
        distance += distance;
    }

    public void upDateEatenPieces() {
        eatenPieces++;
    }

    public ArrayList<Position> getList() {
        return locations;

    }

    @Override
    public ConcretePlayer getOwner() {
        return this.owner;
    }

    @Override
    public String getType() {
        return "♕";
    }
}
