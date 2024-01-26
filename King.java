import java.util.ArrayList;
import java.util.Iterator;

public class King extends ConcretePiece {
    private ConcretePlayer owner;

    private Position currentPosition;
    private ArrayList<Position> locations;
    private static int eatenPieces = 0;
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
    public void typeAndNumber(){
        System.out.print("K7:");
    }
    void printLocations(){
        typeAndNumber();
        System.out.print(" [");
        Iterator i = locations.iterator();
        while(i.hasNext()){
            Position p= (Position)i.next();
            System.out.print(p.toString());
            if(!(i.toString().compareTo((locations.get(locations.size()-1)).toString()) == 0)){
                System.out.print(",");
            }
        }
        System.out.println("]");
    }
    public  Position getCurrentPosition(){
        return currentPosition;
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
