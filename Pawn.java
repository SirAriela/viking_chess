import java.util.ArrayList;
import java.util.Iterator;

public class Pawn extends ConcretePiece {

    private ConcretePlayer owner;
    private Position currentPosition;
    private ArrayList<Position> locations;
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


    @Override
    public ConcretePlayer getOwner() {
        return this.owner;
    }

    public String getType() {
        return this.getOwner().isPlayerOne() ? "♙" : "♟";
    }

    public void upDateDistance(int distance) {
        this.distance += distance;
    }

    public int getDistance() {
        return this.distance;
    }

    public void typeAndNumber() {
        if (this.getOwner().isPlayerOne()) {
            System.out.print("A" + this.pawnNumber + ":");
        } else System.out.print("D" + this.pawnNumber + ":");
    }

    public ArrayList<Position> getList() {
        return locations;

    }

    void printLocations() {
        typeAndNumber();
        System.out.print(" [");
        Iterator i = locations.iterator();
        int n = locations.size();
        Position last = (Position) locations.get(n - 1);
        while (i.hasNext()) {
            Position p = (Position) i.next();
            System.out.print(p.toString());
            if (!p.toString().equals(last.toString())) {
                System.out.print(",");
            }
        }
        System.out.println("]");
    }

    void printDistance() {
        typeAndNumber();
        System.out.println(" " + getDistance() + " squares");
    }
}