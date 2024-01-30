import java.util.ArrayList;
import java.util.Iterator;

public class King extends ConcretePiece {
    private ConcretePlayer owner;

    private Position currentPosition;
    private ArrayList<Position> locations;

    private int distance;

    public King(ConcretePlayer owner, Position position) {
        this.owner = owner;
        this.currentPosition = position;
        this.locations = new ArrayList<Position>();
        locations.add(position);
        this.distance = 0;
    }

    public void upDatePositions(Position position) {
        this.currentPosition = position;
        this.locations.add(position);
    }

    public void typeAndNumber() {
        System.out.print("K7:");
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

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void upDateDistance(int distance) {
        this.distance += distance;
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

    public int getDistance() {
        return this.distance;
    }

    void printDistance() {
        typeAndNumber();
        System.out.println(" " + getDistance() + " squares");
    }
}
