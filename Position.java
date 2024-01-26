public class Position {
    private int x;
    private int y;



    public Position(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public int getpositionX() {
        return this.x;
    }

    public int getpositionY() {
        return this.y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public String toString(){
        return "(" + this.getpositionX() +"," + this.getpositionY() + ")";
    }


}